<template>
    
    <Carousel></Carousel>
    <br>
    <div class="search-box">
        <n-input-group>
            <n-input v-model:value="searchName"></n-input>
            <n-button type="primary" @click="getGameListWithName">搜索</n-button>
        </n-input-group>
    </div>
    <br>
    <n-flex justify="center">
        <n-flex justify="center">
            <GameCard v-for="game in gameList" :gameCoverUrl="game.gameCoverUrl" :gameName="game.gameName" :game-rate="game.gameRate" 
                :gameDescription="game.gameDescription" :game-id="game.gameId" />
        </n-flex>
    </n-flex>
        <div><n-pagination v-model:page="page" :page-count="pageCount"/></div>
           
    <br>
    <br>
    <br>
    <br>
    <!-- <FlvPlayer></FlvPlayer> -->
</template>



<script setup lang="ts">
// import FlvPlayer from '@/components/FlvPlayer.vue'
import Carousel from '@/components/Carousel.vue';
import GameCard from '@/components/GameCard.vue'
import {ref,onMounted ,watch} from 'vue'
const gameList = ref()
const pageCount = ref()
const page = ref()
import axios from 'axios';

onMounted(() => {
    axios.post("http://localhost:8080/gameList?pageNum=1&pageSize=12").then(resp => {
        gameList.value= resp.data.data.list
        pageCount.value = resp.data.data.pages
        page.value =resp.data.data.pageNum
    })
})

watch(page,(newValue,oldValue)=>{
    getGameList()

})

const searchName = ref('')
const queryName=ref('')
function getGameListWithName(){
    axios.post("http://localhost:8080/gameList?pageNum="+page.value+"&pageSize=12&gameName="+searchName.value).then(resp => {
        gameList.value= resp.data.data.list
        pageCount.value = resp.data.data.pages
        queryName.value=searchName.value
        
    })
}
function getGameList(){
        axios.post("http://localhost:8080/gameList?pageNum="+page.value+"&pageSize=12&gameName="+queryName.value).then(resp => {
        gameList.value= resp.data.data.list
        pageCount.value = resp.data.data.pages
        
    })

}

</script>



<style scoped>
.search-box {
    width: 800px;
    margin: 0 auto;
}
</style>