<template>
  <div class="app-layout">
    <aside class="sidebar">
      <div class="sidebar-inner">
        <AiDoctorMascot />
        <div class="brand-block">
          <h1 class="brand-title">硅谷小智</h1>
          <p class="brand-sub">医疗智能助手 · AI + 医生</p>
          <span class="brand-badge">
            <el-icon class="badge-icon"><Cpu /></el-icon>
            伴诊 · 分诊 · 挂号
          </span>
        </div>
        <el-button
          class="new-chat-button"
          type="primary"
          plain
          round
          :icon="Plus"
          @click="newChat"
        >
          新会话
        </el-button>
        <p class="sidebar-hint">
          回答仅供参考，请及时就医并遵医嘱。
        </p>
      </div>
    </aside>

    <main class="main-panel">
      <div class="chat-shell">
        <div class="message-list" ref="messaggListRef">
          <div
            v-for="(message, index) in messages"
            :key="index"
            class="message-row"
            :class="message.isUser ? 'is-user' : 'is-bot'"
          >
            <div class="avatar-icon" :class="message.isUser ? 'user' : 'bot'">
              <el-icon v-if="message.isUser"><User /></el-icon>
              <el-icon v-else><FirstAidKit /></el-icon>
            </div>
            <div
              class="bubble"
              :class="message.isUser ? 'bubble-user' : 'bubble-bot'"
            >
              <span
                class="bubble-inner"
                :class="{ 'markdown-body': !message.isUser }"
              >
                <span v-html="message.content"></span>
                <span
                  v-if="message.isThinking || message.isTyping"
                  class="loading-dots"
                >
                  <span class="dot"></span>
                  <span class="dot"></span>
                  <span class="dot"></span>
                </span>
              </span>
            </div>
          </div>
        </div>

        <div class="input-bar">
          <el-input
            v-model="inputMessage"
            class="chat-input"
            size="large"
            placeholder="描述症状或咨询挂号、科室等问题…"
            clearable
            @keyup.enter="sendMessage"
          />
          <el-button
            type="primary"
            size="large"
            round
            :loading="isSending"
            :disabled="isSending"
            @click="sendMessage"
          >
            发送
          </el-button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import axios from 'axios'
import { v4 as uuidv4 } from 'uuid'
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import { Plus, User, Cpu, FirstAidKit } from '@element-plus/icons-vue'
import AiDoctorMascot from '@/components/AiDoctorMascot.vue'

marked.use({
  gfm: true,
  breaks: true,
})

const messaggListRef = ref()
const isSending = ref(false)
const uuid = ref()
const inputMessage = ref('')
const messages = ref([])

onMounted(() => {
  initUUID()
  watch(messages, () => scrollToBottom(), { deep: true })
  hello()
})

const scrollToBottom = () => {
  if (messaggListRef.value) {
    messaggListRef.value.scrollTop = messaggListRef.value.scrollHeight
  }
}

const hello = () => {
  sendRequest('你好')
}

const sendMessage = () => {
  if (inputMessage.value.trim()) {
    sendRequest(inputMessage.value.trim())
    inputMessage.value = ''
  }
}

/** 用户消息：按纯文本展示，避免误解析 Markdown */
const renderUserPlain = (text) => {
  if (!text) return ''
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/\n/g, '<br>')
    .replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;')
}

/** 助手回复：Markdown → HTML，并净化防止 XSS */
const renderMarkdown = (text) => {
  if (!text) return ''
  try {
    const html = marked.parse(text, { async: false })
    return DOMPurify.sanitize(html)
  } catch {
    return renderUserPlain(text)
  }
}

const sendRequest = (message) => {
  isSending.value = true
  const userMsg = {
    isUser: true,
    content: renderUserPlain(message),
    isTyping: false,
    isThinking: false,
  }
  if (messages.value.length > 0) {
    messages.value.push(userMsg)
  }

  const botMsg = {
    isUser: false,
    content: '',
    isTyping: true,
    isThinking: false,
  }
  messages.value.push(botMsg)
  const lastMsg = messages.value[messages.value.length - 1]
  scrollToBottom()

  axios
    .post(
      '/api/xiaozhi/chat',
      { memoryId: uuid.value, message },
      {
        responseType: 'text',
        onDownloadProgress: (e) => {
          const fullText = e.event.target.responseText || ''
          lastMsg.content = renderMarkdown(fullText)
          scrollToBottom()
        },
      }
    )
    .then(() => {
      messages.value.at(-1).isTyping = false
      isSending.value = false
    })
    .catch((error) => {
      console.error('流式错误:', error)
      messages.value.at(-1).content = renderUserPlain('请求失败，请重试')
      messages.value.at(-1).isTyping = false
      isSending.value = false
    })
}

const initUUID = () => {
  let storedUUID = localStorage.getItem('user_uuid')
  if (!storedUUID) {
    storedUUID = uuidToNumber(uuidv4())
    localStorage.setItem('user_uuid', storedUUID)
  }
  uuid.value = storedUUID
}

const uuidToNumber = (uuidStr) => {
  let number = 0
  for (let i = 0; i < uuidStr.length && i < 6; i++) {
    const hexValue = uuidStr[i]
    number = number * 16 + (parseInt(hexValue, 16) || 0)
  }
  return number % 1000000
}

const newChat = () => {
  localStorage.removeItem('user_uuid')
  window.location.reload()
}
</script>

<style scoped>
.app-layout {
  display: flex;
  height: 100vh;
  min-height: 480px;
  background: linear-gradient(135deg, #e0f2fe 0%, #f8fafc 38%, #eef2ff 100%);
}

.sidebar {
  width: 280px;
  flex-shrink: 0;
  padding: 24px 20px;
  background: linear-gradient(180deg, #0f766e 0%, #115e59 48%, #134e4a 100%);
  color: #ecfdf5;
  box-shadow: 8px 0 32px rgba(15, 118, 110, 0.25);
}

.sidebar-inner {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  gap: 16px;
}

.brand-block {
  text-align: center;
  width: 100%;
}

.brand-title {
  margin: 0;
  font-size: 1.35rem;
  font-weight: 700;
  letter-spacing: 0.06em;
  color: #fff;
}

.brand-sub {
  margin: 8px 0 0;
  font-size: 0.8125rem;
  opacity: 0.92;
  line-height: 1.45;
}

.brand-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-top: 12px;
  padding: 6px 12px;
  border-radius: 999px;
  font-size: 0.75rem;
  background: rgba(255, 255, 255, 0.14);
  border: 1px solid rgba(255, 255, 255, 0.28);
  backdrop-filter: blur(8px);
}

.badge-icon {
  font-size: 14px;
}

.new-chat-button {
  width: 100%;
  margin-top: 8px;
  font-weight: 600;
  border-color: rgba(255, 255, 255, 0.45) !important;
  color: #fff !important;
  background: rgba(255, 255, 255, 0.12) !important;
}

.new-chat-button:hover {
  background: rgba(255, 255, 255, 0.22) !important;
  border-color: rgba(255, 255, 255, 0.65) !important;
}

.sidebar-hint {
  margin-top: auto;
  padding: 12px 10px 0;
  font-size: 0.6875rem;
  line-height: 1.5;
  opacity: 0.72;
  text-align: center;
}

.main-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  padding: 20px 24px 24px;
}

.chat-shell {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
  background: #fff;
  border-radius: 16px;
  box-shadow:
    0 1px 3px rgba(15, 23, 42, 0.06),
    0 12px 40px rgba(37, 99, 235, 0.08);
  border: 1px solid rgba(148, 163, 184, 0.35);
  overflow: hidden;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 20px 18px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  background: linear-gradient(180deg, #fafbfc 0%, #fff 120px);
}

.message-row {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  max-width: 100%;
}

.message-row.is-user {
  flex-direction: row-reverse;
}

.avatar-icon {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: grid;
  place-items: center;
  font-size: 18px;
}

.avatar-icon.user {
  background: linear-gradient(145deg, #38bdf8, #2563eb);
  color: #fff;
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.35);
}

.avatar-icon.bot {
  background: linear-gradient(145deg, #2dd4bf, #0d9488);
  color: #fff;
  box-shadow: 0 4px 12px rgba(13, 148, 136, 0.35);
}

.bubble {
  max-width: min(720px, 85%);
  border-radius: 14px;
  padding: 12px 14px;
  line-height: 1.55;
  font-size: 0.9375rem;
}

.bubble-user {
  background: linear-gradient(145deg, #e0f2fe 0%, #dbeafe 100%);
  color: #0f172a;
  border: 1px solid rgba(59, 130, 246, 0.25);
  border-bottom-right-radius: 6px;
}

.bubble-bot {
  background: #fff;
  color: #1e293b;
  border: 1px solid rgba(148, 163, 184, 0.35);
  border-bottom-left-radius: 6px;
  box-shadow: 0 2px 8px rgba(15, 23, 42, 0.04);
}

.bubble-inner {
  word-break: break-word;
}

/* Markdown 排版（仅助手气泡） */
.markdown-body :deep(p) {
  margin: 0.35em 0;
  line-height: 1.65;
}

.markdown-body :deep(p:first-child) {
  margin-top: 0;
}

.markdown-body :deep(p:last-child) {
  margin-bottom: 0;
}

.markdown-body :deep(h1),
.markdown-body :deep(h2),
.markdown-body :deep(h3),
.markdown-body :deep(h4) {
  margin: 0.65em 0 0.35em;
  font-weight: 700;
  color: #0f172a;
  line-height: 1.35;
}

.markdown-body :deep(h1) {
  font-size: 1.25rem;
}

.markdown-body :deep(h2) {
  font-size: 1.1rem;
}

.markdown-body :deep(h3),
.markdown-body :deep(h4) {
  font-size: 1rem;
}

.markdown-body :deep(ul),
.markdown-body :deep(ol) {
  margin: 0.35em 0;
  padding-left: 1.35rem;
}

.markdown-body :deep(li) {
  margin: 0.2em 0;
}

.markdown-body :deep(li > p) {
  margin: 0.15em 0;
}

.markdown-body :deep(strong) {
  font-weight: 700;
  color: #0f172a;
}

.markdown-body :deep(code) {
  font-family: ui-monospace, 'Cascadia Code', 'SF Mono', Menlo, monospace;
  font-size: 0.86em;
  padding: 0.12em 0.38em;
  border-radius: 6px;
  background: #f1f5f9;
  color: #0f766e;
}

.markdown-body :deep(pre) {
  margin: 0.5em 0;
  padding: 12px 14px;
  border-radius: 10px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  overflow-x: auto;
  font-size: 0.84rem;
  line-height: 1.5;
}

.markdown-body :deep(pre code) {
  padding: 0;
  background: none;
  color: #334155;
  font-size: inherit;
}

.markdown-body :deep(blockquote) {
  margin: 0.45em 0;
  padding: 0.35em 0 0.35em 0.85em;
  border-left: 4px solid #2dd4bf;
  color: #475569;
  background: rgba(45, 212, 191, 0.08);
  border-radius: 0 8px 8px 0;
}

.markdown-body :deep(a) {
  color: #2563eb;
  text-decoration: none;
}

.markdown-body :deep(a:hover) {
  text-decoration: underline;
}

.markdown-body :deep(hr) {
  margin: 0.75em 0;
  border: none;
  border-top: 1px solid #e2e8f0;
}

.markdown-body :deep(table) {
  border-collapse: collapse;
  margin: 0.5em 0;
  font-size: 0.88rem;
}

.markdown-body :deep(th),
.markdown-body :deep(td) {
  border: 1px solid #e2e8f0;
  padding: 6px 10px;
  text-align: left;
}

.markdown-body :deep(th) {
  background: #f8fafc;
  font-weight: 600;
}

.loading-dots {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-left: 6px;
  vertical-align: middle;
}

.dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #64748b;
  animation: pulse 1.2s infinite ease-in-out both;
}

.dot:nth-child(2) {
  animation-delay: -0.25s;
}

.dot:nth-child(3) {
  animation-delay: -0.5s;
}

@keyframes pulse {
  0%,
  100% {
    transform: translateY(0);
    opacity: 0.35;
  }
  50% {
    transform: translateY(-3px);
    opacity: 1;
  }
}

.input-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-top: 1px solid rgba(226, 232, 240, 0.9);
  background: #f8fafc;
}

.chat-input {
  flex: 1;
}

.chat-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 0 0 1px rgba(148, 163, 184, 0.35) inset;
}

@media (max-width: 768px) {
  .app-layout {
    flex-direction: column;
    height: 100vh;
    height: 100dvh;
  }

  .sidebar {
    width: 100%;
    flex-direction: row;
    padding: 12px 16px;
    box-shadow: 0 8px 24px rgba(15, 118, 110, 0.2);
  }

  .sidebar-inner {
    flex-direction: row;
    flex-wrap: wrap;
    align-items: center;
    justify-content: space-between;
    gap: 10px;
    height: auto;
  }

  .brand-block {
    text-align: left;
    flex: 1;
    min-width: 0;
  }

  .brand-title {
    font-size: 1.1rem;
  }

  .brand-sub {
    margin-top: 2px;
    font-size: 0.75rem;
  }

  .brand-badge {
    margin-top: 6px;
    font-size: 0.6875rem;
    padding: 4px 10px;
  }

  .new-chat-button {
    width: auto;
    margin-top: 0;
    flex-shrink: 0;
  }

  .sidebar-hint {
    display: none;
  }

  .main-panel {
    padding: 12px;
    flex: 1;
    min-height: 0;
  }

  .bubble {
    max-width: 92%;
  }
}
</style>
