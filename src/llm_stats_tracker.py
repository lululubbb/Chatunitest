"""
LLMStatsTracker — drop-in class matching the refine-agent LLMStatsTracker interface.

Tracks:
  - prompt_tokens / completion_tokens from each API call's usage dict
  - llm_elapsed_time: wall-clock seconds of the LLM API call only
    (NOT including compile/run/repair time)

Usage:
    tracker = LLMStatsTracker()
    tracker.record(prompt_tokens=120, completion_tokens=300, elapsed=2.5)
    ...
    summary = tracker.summary()   # dict with totals + per-round list
"""

import time
from dataclasses import dataclass, field
from typing import List, Dict, Any


@dataclass
class _CallRecord:
    round_idx: int
    prompt_tokens: int
    completion_tokens: int
    llm_elapsed_seconds: float


class LLMStatsTracker:
    """
    Mirrors the refine-agent LLMStatsTracker.

    Key design decisions (to match refine agent):
      - elapsed = wall-clock of the LLM HTTP call only
      - tokens  = values from response["usage"]["prompt_tokens"] /
                  response["usage"]["completion_tokens"]
      - No compile / run / repair time is included in elapsed
    """

    def __init__(self):
        self._records: List[_CallRecord] = []
        self._round = 0          # current round counter (incremented by caller)

    # ------------------------------------------------------------------
    # Public API
    # ------------------------------------------------------------------

    def next_round(self):
        """Advance the round counter (call once per repair iteration)."""
        self._round += 1

    def record(self, prompt_tokens: int, completion_tokens: int,
               elapsed: float, round_idx: int = None):
        """
        Record one LLM API call.

        Parameters
        ----------
        prompt_tokens     : from response["usage"]["prompt_tokens"]
        completion_tokens : from response["usage"]["completion_tokens"]
        elapsed           : wall-clock seconds of the API call
        round_idx         : explicit round number; uses internal counter if None
        """
        r = round_idx if round_idx is not None else self._round
        self._records.append(_CallRecord(
            round_idx=r,
            prompt_tokens=int(prompt_tokens),
            completion_tokens=int(completion_tokens),
            llm_elapsed_seconds=float(elapsed),
        ))

    def record_from_response(self, response: dict, elapsed: float,
                              round_idx: int = None):
        """
        Convenience wrapper: extract tokens directly from an API response dict.

        Compatible with both OpenAI and Ollama response shapes:
            response["usage"]["prompt_tokens"]
            response["usage"]["completion_tokens"]
        """
        usage = response.get("usage", {})
        pt = int(usage.get("prompt_tokens", 0))
        ct = int(usage.get("completion_tokens", 0))
        self.record(pt, ct, elapsed, round_idx=round_idx)

    # ------------------------------------------------------------------
    # Aggregation
    # ------------------------------------------------------------------

    @property
    def total_prompt_tokens(self) -> int:
        return sum(r.prompt_tokens for r in self._records)

    @property
    def total_completion_tokens(self) -> int:
        return sum(r.completion_tokens for r in self._records)

    @property
    def total_tokens(self) -> int:
        return self.total_prompt_tokens + self.total_completion_tokens

    @property
    def total_llm_elapsed(self) -> float:
        """Total wall-clock seconds spent waiting for LLM responses."""
        return sum(r.llm_elapsed_seconds for r in self._records)

    @property
    def num_calls(self) -> int:
        return len(self._records)

    def summary(self) -> Dict[str, Any]:
        """
        Return a summary dict that is compatible with the existing
        token_stats / time_stats format used in askGPT.whole_process(),
        AND with the refine-agent LLMStatsTracker.summary() schema.
        """
        rounds: Dict[int, Dict[str, Any]] = {}
        for rec in self._records:
            r = rec.round_idx
            if r not in rounds:
                rounds[r] = {
                    "prompt_tokens": 0,
                    "completion_tokens": 0,
                    "total_tokens": 0,
                    "llm_elapsed_seconds": 0.0,
                    "num_calls": 0,
                }
            rounds[r]["prompt_tokens"]      += rec.prompt_tokens
            rounds[r]["completion_tokens"]  += rec.completion_tokens
            rounds[r]["total_tokens"]       += rec.prompt_tokens + rec.completion_tokens
            rounds[r]["llm_elapsed_seconds"] += rec.llm_elapsed_seconds
            rounds[r]["num_calls"]          += 1

        return {
            # ── totals (match refine-agent schema) ──────────────────────
            "total_prompt_tokens":     self.total_prompt_tokens,
            "total_completion_tokens": self.total_completion_tokens,
            "total_tokens":            self.total_tokens,
            "total_llm_elapsed_seconds": self.total_llm_elapsed,
            "num_llm_calls":           self.num_calls,

            # ── per-round breakdown ──────────────────────────────────────
            "rounds": {f"round_{k}": v for k, v in sorted(rounds.items())},

            # ── legacy keys (keep backward compat with existing CSVs) ───
            "rounds_token_stats": {
                f"round_{k}": {
                    "prompt_tokens":     v["prompt_tokens"],
                    "completion_tokens": v["completion_tokens"],
                    "total_tokens":      v["total_tokens"],
                }
                for k, v in sorted(rounds.items())
            },
        }