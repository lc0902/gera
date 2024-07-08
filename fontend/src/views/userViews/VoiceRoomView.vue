<template>
    <h1>
        {{ roomName }}
    </h1>
    <p>
        退出该页面即可退出房间，房主主动退出该房间则关闭房间
    </p>
    <br>
    房主
    <div style="display: flex;align-items: center;">
        {{ ownerName }}
        <n-avatar :src="ownerAvatarUrl"></n-avatar>
    </div>
    <br><br>
    成员：
    <n-flex vertical>
        <div  v-for="i in users">
            <h1>
                <n-avatar :src="i.userAvatarUrl"></n-avatar>
                {{ i.userName }}

            </h1>
        </div>
    </n-flex>
    
</template>



<script setup lang="ts">
import { useCookies } from 'vue3-cookies';
import { onMounted, onUnmounted, ref } from "vue"
import { useUserStore } from '@/stores/user';
import axios from 'axios';
const users = ref([] as any[])
const userStore = useUserStore();
import { useRouter } from 'vue-router';
const router = useRouter()
import { useMessage } from 'naive-ui';
const message = useMessage()

const cookies = useCookies().cookies;

const mediaStack = ref()
const audioCtx = ref()
const scriptNode = ref()
const source = ref()
const play = ref(true)
const wsVoice = ref()
const wsMember = ref()

function initWsVoice() {
    wsVoice.value = new WebSocket("ws://localhost:8080/voice/" + cookies.get("userToken") + "/" + cookies.get("currentRoomId"))
    wsVoice.value.onopen = () => {
        console.log('socket 已连接')
    }
    wsVoice.value.binaryType = 'arraybuffer'
    wsVoice.value.onmessage = ({ data }:any) => {
        // console.log("接收到的数据--》" + data)
    


        // 将接收的数据转换成与传输过来的数据相同的Float32Array
        const buffer = new Float32Array(data)
        // 创建一个空白的AudioBuffer对象，这里的4096跟发送方保持一致，48000是采样率
        const myArrayBuffer = audioCtx.value.createBuffer(1, 4096, 48000)
        // 也是由于只创建了一个音轨，可以直接取到0
        const nowBuffering = myArrayBuffer.getChannelData(0)
        // 通过循环，将接收过来的数据赋值给简单音频对象
        for (let i = 0; i < 4096; i++) {
            nowBuffering[i] = buffer[i]
        }
        // 使用AudioBufferSourceNode播放音频
        const source = audioCtx.value.createBufferSource()
        source.buffer = myArrayBuffer
        const gainNode = audioCtx.value.createGain()
        source.connect(gainNode)
        gainNode.connect(audioCtx.value.destination)
        var muteValue = 1
        if (!play.value) { // 是否静音
            muteValue = 0
        }
        gainNode.gain.setValueAtTime(muteValue, audioCtx.value.currentTime)
        source.start()
    }
    wsVoice.value.onerror = (e: any) => {
        console.log('发生错误', e)
        router.push("/")
        message.info("已退出房间")
    }
    wsVoice.value.onclose = () => {
        console.log('socket closed')
        router.push("/")
        message.info("已退出房间")
        
    }
}

function initWsMember() {
    wsMember.value = new WebSocket("ws://localhost:8080/member/" + cookies.get("userToken") + "/" + cookies.get("currentRoomId"))
    wsMember.value.onopen = () =>{
        console.log("wsMember 连接成功");
    }
    wsMember.value.onmessage = (data:any) => {
        console.log("wsMember 接收到的数据--》" + data);
        // console.log(data.data)
        users.value = []
        users.value = JSON.parse(data.data).users
        // console.log(users.value)

    }

}



onMounted(() => {
    startCall();
    initWsMember()
    getRoomInfo()
})

onUnmounted(() => {
    stopCall();
    wsVoice.value.close()
    for(var i = 1; i <100 ;i++){

    }
    wsMember.value.close()
})



function startCall() {

play.value = true
audioCtx.value = new AudioContext()
initWsVoice()

// 该变量存储当前MediaStreamAudioSourceNode的引用
// 可以通过它关闭麦克风停止音频传输

// 创建一个ScriptProcessorNode 用于接收当前麦克风的音频
scriptNode.value = audioCtx.value.createScriptProcessor(4096, 1, 1)
navigator.mediaDevices
  .getUserMedia({ audio: true, video: false })
  .then((stream) => {
    mediaStack.value = stream
    source.value = audioCtx.value.createMediaStreamSource(stream)

    source.value.connect(scriptNode.value)
    scriptNode.value.connect(audioCtx.value.destination)
  })
  .catch(function (err) {
    /* 处理error */
    console.log('err', err)
  })
// 当麦克风有声音输入时，会调用此事件
// 实际上麦克风始终处于打开状态时，即使不说话，此事件也在一直调用
scriptNode.value.onaudioprocess = (audioProcessingEvent: { inputBuffer: any; }) => {
  const inputBuffer = audioProcessingEvent.inputBuffer
  // console.log("inputBuffer",inputBuffer);
  // 由于只创建了一个音轨，这里只取第一个频道的数据
  const inputData = inputBuffer.getChannelData(0)
//   console.log("调用")
  // 通过socket传输数据，实际上传输的是Float32Array
  if (wsVoice.value.readyState === 1) {
      
      
      // console.log("发送的数据",inputData);
    wsVoice.value.send(inputData)
  }
}
}
// 关闭麦克风
function stopCall() {
play.value = false
mediaStack.value.getTracks()[0].stop()
scriptNode.value.disconnect()
}

const roomName = ref("")
const ownerName = ref()
const ownerAvatarUrl = ref("")

function getRoomInfo(){
    axios.post("http://localhost:8080/getRoomInfo?roomId="+cookies.get("currentRoomId")).then(resp=>{
        console.log(resp.data);

        roomName.value = resp.data.data.roomBean.roomName
        ownerName.value = resp.data.data.owner.userName
        ownerAvatarUrl.value = resp.data.data.owner.userAvatarUrl
    })
}
</script>


<style scoped></style>