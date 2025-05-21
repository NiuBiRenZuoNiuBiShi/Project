<template>
  <!-- 全屏滚动容器 -->
  <div class="fullpage-container">
    <!-- 第一页 - 完全保留原有结构和样式 -->
    <section class="section original-page">
      <header>
        <div class="main">
          <ul>
            <li class="active"><a href="http://localhost:5173/login">登录</a></li>
            <li class="active"><a href="http://localhost:5173/register">注册</a></li>
            <li class="active"><a href="http://localhost:5173/home">主页</a></li>
          </ul>
        </div>
        <div class="title">
          <h1><span style="color: crimson;">About</span> Us</h1>
        </div>
      </header>
    </section>

    <!-- 第二页 - 图片和文字分左右 -->
    <section class="section new-page" :class="{'page-enter': isSecondPageVisible}">
      <div class="content">
        <div class="left-side">
          <!-- 所有图片都叠加在一起 -->
          <img class="overlay-image" src="https://cdn.pixabay.com/photo/2021/01/05/11/19/bled-5890874_1280.jpg" alt="Beautiful View" />
          <img class="overlay-image" src="https://cdn.pixabay.com/photo/2024/01/12/21/23/cortina-dampezzo-8504755_1280.jpg" alt="Image 1" />
          <img class="overlay-image" src="https://media.istockphoto.com/id/2130934199/zh/%E7%85%A7%E7%89%87/amazing-scenery-at-a-mountain-lake-in-the-bavarian-alps.jpg?s=612x612&w=0&k=20&c=LW9vHuHUdfIKI8giozhCgSUdJhHQlXyCt8gNSocaVls=" alt="Image 2" />
          <img class="overlay-image" src="https://media.istockphoto.com/id/1066331604/zh/%E7%85%A7%E7%89%87/%E5%BE%9E%E9%A0%82%E8%A6%96%E5%9C%96%E5%8F%AF%E4%BB%A5%E7%9C%8B%E5%88%B0%E6%B0%B4%E9%9D%A2%E4%B8%8A%E7%9A%84%E9%81%8A%E8%89%87%E5%BE%9E%E9%A0%82%E9%83%A8%E7%9A%84%E8%A6%96%E5%9C%96%E7%B6%A0%E6%9D%BE%E7%9F%B3%E6%B0%B4%E7%9A%84%E8%83%8C%E6%99%AF%E4%BE%86%E8%87%AA%E7%A9%BA%E6%B0%A3%E7%9A%84%E5%A4%8F%E5%AD%A3%E6%B5%B7%E6%99%AF%E6%97%85%E9%81%8A%E7%90%86%E5%BF%B5%E5%92%8C%E7%90%86%E5%BF%B5.jpg?s=612x612&w=0&k=20&c=q4oWKfX47Zh6Bl-uxHptMgMxlC4tn8z-_VDVWckDLsk=" alt="Image 3" />
        </div>
        <div class="right-side">
          <h2>What We Do?</h2>
          <p>我们致力于为旅行者提供便捷的订票和订餐服务</p>
          <p>帮助他们规划完美的旅行并享受美食体验</p>
          <p>我们的团队专注于设计和开发数字化旅行和餐饮解决方案</p>
          <p>让每个旅行和餐饮体验都充满惊喜与便捷</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const isSecondPageVisible = ref(false);

// 滚动检测
onMounted(() => {
  let isScrolling = false;

  // 页面滚动到第二页
  window.addEventListener('wheel', (e) => {
    if (isScrolling) return;

    // 仅在下滑且位于第一页时触发
    if (e.deltaY > 0 && window.scrollY === 0) {
      isScrolling = true;
      window.scrollTo({
        top: window.innerHeight,
        behavior: 'smooth'
      });
      setTimeout(() => {
        isSecondPageVisible.value = true;  // 第二页开始显示动画
      }, 300);
      setTimeout(() => isScrolling = false, 1000);
    }
  });
});
</script>

<style scoped>
/* 第一页原有样式 - 完全保持不变 */
* {
  padding: 0;
  margin: 0;
  font-family: "楷体";
}

header {
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
  url(https://images.pexels.com/photos/1083822/pexels-photo-1083822.jpeg?cs=srgb&dl=pexels-lisa-1083822.jpg&fm=jpg);
  background-size: cover;
  background-position: center;
  height: 100vh;
  width: 100vw;
  position: relative;
}

ul {
  position: fixed;
  top: 20px;
  right: 20px;
  list-style-type: none;
  margin: 15px;
  z-index: 1000;
}

ul li {
  display: inline-block;
}

ul li a {
  text-decoration: none;
  color: #fff;
  padding: 5px 20px;
  border: 1px solid transparent;
  transition: all 0.3s ease;
  border-radius: 20px;
  background-color: transparent;
}
ul li a:active {
  background-color: #ff6347;
  color: #fff;
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

ul li.active a:hover {
  background-color: #ff6347;
  color: white;
}

ul li a:hover {
  background-color: #fff;
  color: #000;
}

ul li.active a {
  background-color: #fff;
  color: #000;
  font-weight: bold;
}

.title {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.title h1 {
  font-size: 10vw;
  font-family: Century Gothic;
  color: #fff;
  text-shadow: 2px 2px 5px rgba(0, 0, 0, 0.5);
}

/* 新增的翻页样式 */
.fullpage-container {
  height: 100vh;
  overflow-y: auto;
  scroll-snap-type: y mandatory;
}

.section {
  height: 100vh;
  width: 100vw;
  scroll-snap-align: start;
}

/* 第二页的样式 - 背景图片 */
.new-page {
  background-image: url('https://cdn.pixabay.com/photo/2020/05/09/22/16/china-5151605_1280.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  opacity: 0;  /* 初始时隐藏第二页 */
  transition: opacity 1s ease-in-out; /* 动画过渡效果 */
}

/* 第二页动画效果 - 显示时改变透明度 */
.page-enter {
  opacity: 1; /* 滚动进入后变得可见 */
}

/* 第二页内容 */
.new-page .content {
  display: flex;
  height: 100%;
  align-items: center;
  justify-content: space-between;
  padding: 0 1rem;
  margin: 0;
}

.left-side {
  position: relative;
  flex: 0 0 50%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
  margin: 0;
}

.left-side img {
  width: 100%;
  height: auto;
  border-radius: 10px;
}

.right-side {
  position: relative;
  color: white;
  text-align: left;
  padding: 1rem;
  z-index: 1000;
  max-width: 45%;
  flex: 0 0 45%;
  margin-top: -2rem;
}

.right-side h2 {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.right-side p {
  position: relative;
  font-size: 2rem;
  margin-bottom: 1rem;
  line-height: 1.6;
  text-align: left !important;
  color: #ffffff;
}

/* 四张图片的叠加效果 */
.overlay-image {
  position: absolute;
  width: 500px !important;
  height: 400px !important;
  object-fit: cover;
  border-radius: 10px;
  opacity: 0.8;
  transition: all 0.3s ease;
  border: 2px solid white;
  top: 10%;
  left: 20%;
}

/* 散乱叠加效果 */
.overlay-image:nth-child(1) {
  top: 10%;
  left: 5%;
  transform: rotate(2deg);
}

.overlay-image:nth-child(2) {
  top: 30%;
  left: 10%;
  transform: rotate(-5deg);
}

.overlay-image:nth-child(3) {
  top: 40%;
  left: 25%;
  transform: rotate(3deg);
}

.overlay-image:nth-child(4) {
  top: 50%;
  left: 45%;
  transform: rotate(-7deg);
}

.overlay-image:hover {
  opacity: 1;
  transform: scale(1.05);
}

/* 响应式调整：确保页面适配小屏幕 */
@media (max-width: 768px) {
  .title h1 {
    font-size: 12vw;
  }

  .new-page .content {
    flex-direction: column;
  }

  .left-side, .right-side {
    flex: 0 0 100%;
  }

  .left-side img {
    max-width: 100%;
    border-radius: 5px;
  }

  .overlay-image {
    width: 70px;
  }
}

@media (max-width: 480px) {
  .title h1 {
    font-size: 15vw;
  }
}
</style>
