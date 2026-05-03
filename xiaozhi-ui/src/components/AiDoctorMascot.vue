<template>
  <div class="mascot-wrap" aria-hidden="true">
    <svg
      class="mascot-svg"
      viewBox="0 0 200 200"
      xmlns="http://www.w3.org/2000/svg"
    >
      <defs>
        <linearGradient id="bgGrad" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" stop-color="#0d9488" />
          <stop offset="55%" stop-color="#2563eb" />
          <stop offset="100%" stop-color="#7c3aed" />
        </linearGradient>
        <linearGradient id="coatGrad" x1="0%" y1="0%" x2="0%" y2="100%">
          <stop offset="0%" stop-color="#f8fafc" />
          <stop offset="100%" stop-color="#e2e8f0" />
        </linearGradient>
        <filter id="softShadow" x="-20%" y="-20%" width="140%" height="140%">
          <feDropShadow
            dx="0"
            dy="4"
            stdDeviation="6"
            flood-color="#0f172a"
            flood-opacity="0.18"
          />
        </filter>
      </defs>
      <!-- 外圈 -->
      <circle cx="100" cy="100" r="92" fill="url(#bgGrad)" filter="url(#softShadow)" />
      <circle cx="100" cy="100" r="84" fill="#0f172a" opacity="0.08" />
      <!-- AI 电路点缀 -->
      <path
        d="M38 58h16v6H38zm108 78h16v6h-16zM52 140l10-10M148 68l10-10"
        stroke="#fff"
        stroke-width="3"
        stroke-linecap="round"
        opacity="0.35"
      />
      <circle cx="52" cy="52" r="5" fill="#a5f3fc" opacity="0.9" />
      <circle cx="156" cy="62" r="4" fill="#c4b5fd" opacity="0.95" />
      <circle cx="148" cy="148" r="5" fill="#6ee7b7" opacity="0.85" />

      <!-- 头部 -->
      <ellipse cx="100" cy="72" rx="34" ry="36" fill="#fcd9bd" />
      <!-- 头发 -->
      <path
        d="M66 68c4-22 28-38 52-36 24 2 40 18 42 40 2 14-4 26-12 32"
        fill="none"
        stroke="#334155"
        stroke-width="10"
        stroke-linecap="round"
      />
      <path
        d="M70 78c8-18 26-28 46-26 22 2 36 14 38 34"
        fill="#1e293b"
      />
      <!-- 眼镜 / 「智能」视觉 -->
      <rect x="72" y="64" width="24" height="14" rx="4" fill="none" stroke="#0f172a" stroke-width="2.5" />
      <rect x="104" y="64" width="24" height="14" rx="4" fill="none" stroke="#0f172a" stroke-width="2.5" />
      <line x1="96" y1="71" x2="104" y2="71" stroke="#0f172a" stroke-width="2.5" />
      <circle cx="84" cy="71" r="3" fill="#38bdf8" opacity="0.85" />
      <circle cx="116" cy="71" r="3" fill="#38bdf8" opacity="0.85" />

      <!-- 听诊器 -->
      <path
        d="M52 118c18-6 32-4 48 8"
        fill="none"
        stroke="#0f172a"
        stroke-width="4"
        stroke-linecap="round"
      />
      <circle cx="48" cy="122" r="10" fill="#1e293b" stroke="#f8fafc" stroke-width="3" />

      <!-- 白大褂 -->
      <path
        d="M62 112h76l14 78H48l14-78z"
        fill="url(#coatGrad)"
        stroke="#cbd5e1"
        stroke-width="2"
      />
      <path d="M100 112v52" stroke="#94a3b8" stroke-width="2" stroke-dasharray="6 6" />
      <!-- 口袋上的小 AI 芯片 -->
      <rect x="88" y="128" width="24" height="18" rx="3" fill="#e0f2fe" stroke="#0284c7" stroke-width="2" />
      <path
        d="M94 134h12M94 139h8"
        stroke="#0369a1"
        stroke-width="2"
        stroke-linecap="round"
      />
    </svg>
    <span class="mascot-ring" />
  </div>
</template>

<style scoped>
.mascot-wrap {
  position: relative;
  width: 148px;
  height: 148px;
  display: grid;
  place-items: center;
}

.mascot-svg {
  width: 100%;
  height: 100%;
  display: block;
  position: relative;
  z-index: 1;
}

.mascot-ring {
  position: absolute;
  inset: -6px;
  border-radius: 50%;
  border: 2px solid rgba(255, 255, 255, 0.45);
  box-shadow:
    0 0 0 1px rgba(15, 23, 42, 0.06),
    0 12px 28px rgba(37, 99, 235, 0.15);
  pointer-events: none;
  z-index: 0;
}

@media (max-width: 768px) {
  .mascot-wrap {
    width: 52px;
    height: 52px;
  }
  .mascot-ring {
    inset: -3px;
  }
}
</style>
