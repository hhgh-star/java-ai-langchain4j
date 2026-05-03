<template>
  <ChatWindow/>
</template>

<script setup>
import ChatWindow from '@/components/ChatWindow.vue'
</script>

<style>
html,
body {
  margin: 0;
  height: 100%;
  overflow: hidden;
  font-family:
    'Segoe UI',
    system-ui,
    -apple-system,
    'PingFang SC',
    'Microsoft YaHei',
    sans-serif;
  background: #f1f5f9;
  color: #0f172a;
}

#app {
  height: 100%;
  width: 100%;
  max-width: none;
  margin: 0;
  padding: 0;
}
</style>
