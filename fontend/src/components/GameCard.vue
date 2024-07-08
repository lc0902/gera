<template>
    <div class="game-card-box" @click="viewGame()">
        <n-card hoverable>
            <template #cover>
                <img :src="props.gameCoverUrl" width="200px" height="200px" />
            </template>
            <n-performant-ellipsis style="font-size: 24px; color: #8a2be2; max-width: 150px ;">
                {{ props.gameName }}
            </n-performant-ellipsis>
            <n-performant-ellipsis style="max-width: 170px ; " >
                {{ props.gameDescription }}
            </n-performant-ellipsis>
        </n-card>
        <n-rate readonly :default-value="props.gameRate"  allow-half />
    </div>

</template>

<script setup lang="ts">
import {useRouter} from 'vue-router'
const router = useRouter()
const props = defineProps({
    gameId:Number,
    gameName: String,
    gameCoverUrl: String,
    gameDescription: String,
    gameRate: Number
})

function viewGame(){
    router.push("/userView/gameView/"+props.gameId)
}
</script>



<style scoped>
.game-card-box {
    width: 200px;
    height: 320px;
}
</style>