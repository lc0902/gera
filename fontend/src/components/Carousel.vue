<template>
    <div >
        <n-carousel effect="card" prev-slide-style="transform: translateX(-150%) translateZ(-800px);"
            next-slide-style="transform: translateX(50%) translateZ(-800px);" style="height: 300px;"
            :show-dots="false" autoplay>
            <n-carousel-item :style="{ width: '88%' }">
                <img class="carousel-img"
                    src="http://localhost/pic/carousel/1.jpg">
            </n-carousel-item>
            <n-carousel-item :style="{ width: '88%' }">
                <img class="carousel-img"
                    src="http://localhost/pic/carousel/2.jpg">
            </n-carousel-item>
            <n-carousel-item :style="{ width: '88%' }">
                <img class="carousel-img"
                    src="http://localhost/pic/carousel/3.jpg">
            </n-carousel-item>
            <n-carousel-item :style="{ width: '88%' }">
                <img class="carousel-img"
                    src="http://localhost/pic/carousel/4.jpg">
            </n-carousel-item>
        </n-carousel>



    </div>

</template>

<style scoped>
.carousel-img {
    margin: 0 auto;
    width: 100%;
    height: 100%;
    object-fit: cover;
}
</style>