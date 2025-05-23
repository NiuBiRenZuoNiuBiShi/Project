<template>
  <div class="home-view">
    <div class="auth-buttons">
    <button @click="showLoginModal = true" class="auth-button login-button">Login</button>
    <button @click="showRegisterModal = true" class="auth-button register-button">Register</button>
      <button @click="goToAbout" class="auth-button about-button">About</button>
      <!-- Hero Section with 3D effects and morphing background -->
    </div>
    <section class="hero-section">
      <div class="animated-bg"></div>
      <div class="gradient-orbs">
        <div class="orb orb-1"></div>
        <div class="orb orb-2"></div>
        <div class="orb orb-3"></div>
      </div>
      <div class="home-page">
        <!-- 你的主页内容 -->
        <!-- 登录按钮 -->
        <RegisterModal
            v-if="showRegisterModal"
            @close="showRegisterModal = false"
            @switch-to-login="switchToLogin"
        />
        <!-- 登录弹窗 -->
        <LoginModal v-if="showLoginModal" @close="showLoginModal = false" />
      </div>
      <div class="hero-content">
        <h1 class="hero-title">
          <span class="line">Discover</span>
          <span class="line">the World with</span>
          <span class="line accent-text">SwiftTravel</span>
        </h1>
        <p class="hero-subtitle">Your journey begins with a single click</p>
        <div class="hero-cta">
          <button @click="$router.push('/search?form=Train')" class="primary-btn">
            <span>Book Trains</span>
            <i class="fa-solid fa-arrow-right"></i>
          </button>
          <button @click="$router.push('/search?form=Hotel')" class="secondary-btn">
            <span>Book Hotel</span>
            <i class="fa-solid fa-arrow-right"></i>
          </button>
        </div>
      </div>
      <div class="hero-image-container">
        <div class="hero-image">
          <div class="parallax-effect" id="scene"></div>
        </div>
        <div class="floating-card floating-card-1">
          <div class="card-icon"><i class="fa-solid fa-map-location-dot"></i></div>
          <span>1200+ Destinations</span>
        </div>
        <div class="floating-card floating-card-2">
          <div class="card-icon"><i class="fa-solid fa-users"></i></div>
          <span>10M+ Happy Travelers</span>
        </div>
        <div class="floating-card floating-card-3">
          <div class="card-icon"><i class="fa-solid fa-star"></i></div>
          <span>Exceptional Experiences</span>
        </div>
      </div>
    </section>

    <!-- Services Section with flashy cards -->
    <section class="services-section">
      <div class="section-header" data-aos="fade-up">
        <div class="section-tag">EXPLORE OPTIONS</div>
        <h2>Travel Your Way</h2>
        <p>Seamless booking experience for all your travel needs</p>
      </div>

      <div class="service-cards">
        <div class="service-card" tabindex="0" @click="openFlightModal" data-aos="zoom-in" data-aos-delay="100">
          <div class="card-bg"></div>
          <div class="card-icon-wrapper">
            <i class="fa-solid fa-plane"></i>
          </div>
          <h3>Flights</h3>
          <p>Find and book the best deals on flights worldwide with our price matching guarantee</p>
          <span class="card-cta">Search Now <i class="fa-solid fa-arrow-right"></i></span>
        </div>

        <div class="service-card" tabindex="0" @click="$router.push('/search?form=Train')" data-aos="zoom-in" data-aos-delay="200">
          <div class="card-bg"></div>
          <div class="card-icon-wrapper">
            <i class="fa-solid fa-train"></i>
          </div>
          <h3>Trains</h3>
          <p>Book train tickets with meal service options and enjoy a comfortable journey</p>
          <span class="card-cta">Book Now <i class="fa-solid fa-arrow-right"></i></span>
        </div>

        <div class="service-card" tabindex="0" @click="$router.push('/search?form=Hotel')" data-aos="zoom-in" data-aos-delay="300">
          <div class="card-bg"></div>
          <div class="card-icon-wrapper">
            <i class="fa-solid fa-hotel"></i>
          </div>
          <h3>Hotels</h3>
          <p>Discover comfortable stays at great prices with our curated selection of hotels</p>
          <span class="card-cta">Find Hotels <i class="fa-solid fa-arrow-right"></i></span>
        </div>
      </div>
    </section>

    <!-- Features Section with glass morphism -->
    <section class="features-section">
      <div class="features-bg">
        <div class="blob blob-1"></div>
        <div class="blob blob-2"></div>
      </div>

      <div class="section-header" data-aos="fade-up">
        <div class="section-tag">BENEFITS</div>
        <h2>Why Choose SwiftTravel</h2>
        <p>We make your travel planning simple, efficient and enjoyable</p>
      </div>

      <div class="features-grid">
        <div class="feature-item glassmorphism" data-aos="fade-right" data-aos-delay="100">
          <div class="feature-icon">
            <i class="fa-solid fa-shield-halved"></i>
          </div>
          <div class="feature-content">
            <h3>Secure Booking</h3>
            <p>Your personal and payment information is always protected</p>
          </div>
        </div>

        <div class="feature-item glassmorphism" data-aos="fade-left" data-aos-delay="200">
          <div class="feature-icon">
            <i class="fa-solid fa-tag"></i>
          </div>
          <div class="feature-content">
            <h3>Best Price Guarantee</h3>
            <p>Find a lower price elsewhere? We'll match it</p>
          </div>
        </div>

        <div class="feature-item glassmorphism" data-aos="fade-right" data-aos-delay="300">
          <div class="feature-icon">
            <i class="fa-solid fa-headset"></i>
          </div>
          <div class="feature-content">
            <h3>24/7 Support</h3>
            <p>Our support team is always available to help you</p>
          </div>
        </div>

        <div class="feature-item glassmorphism" data-aos="fade-left" data-aos-delay="400">
          <div class="feature-icon">
            <i class="fa-solid fa-utensils"></i>
          </div>
          <div class="feature-content">
            <h3>Train Meal Service</h3>
            <p>Order meals directly through our platform for your train journey</p>
          </div>
        </div>
      </div>
    </section>

    <!-- Testimonials Section with modern cards -->
    <section class="testimonials-section">
      <div class="section-header" data-aos="fade-up">
        <div class="section-tag">REVIEWS</div>
        <h2>What Our Customers Say</h2>
        <p>Join thousands of satisfied travelers</p>
      </div>

      <div class="testimonials-container">
        <div class="testimonial-card" data-aos="flip-left" data-aos-delay="100">
          <div class="testimonial-quote-icon">
            <i class="fa-solid fa-quote-left"></i>
          </div>
          <div class="testimonial-rating">
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
          </div>
          <p class="testimonial-text">"The train meal ordering service saved my trip! Being able to select my food
            before boarding made the journey so much more enjoyable."</p>
          <div class="testimonial-author">
            <div class="author-avatar auther-1"></div>
            <div class="author-info">
              <h4>Sarah J.</h4>
              <p>Business Traveler</p>
            </div>
          </div>
          <div class="testimonial-badge">Verified</div>
        </div>

        <div class="testimonial-card" data-aos="flip-right" data-aos-delay="200">
          <div class="testimonial-quote-icon">
            <i class="fa-solid fa-quote-left"></i>
          </div>
          <div class="testimonial-rating">
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
          </div>
          <p class="testimonial-text">"Found a great hotel deal that was cheaper than anywhere else. The booking process
            was smooth and customer service excellent!"</p>
          <div class="testimonial-author">
            <div class="author-avatar auther-2"></div>
            <div class="author-info">
              <h4>Michael T.</h4>
              <p>Family Vacationer</p>
            </div>
          </div>
          <div class="testimonial-badge">Verified</div>
        </div>
      </div>
    </section>

    <!-- Call-to-action Section -->
    <section class="cta-section">
      <div class="cta-bg">
        <div class="cta-blob"></div>
      </div>
      <div class="cta-content" data-aos="zoom-in">
        <h2>Ready for Your Next Adventure?</h2>
        <p>Start planning your journey today and experience the difference with SwiftTravel</p>
        <button @click="$router.push('/search')" class="cta-button">
          <span>Start Exploring</span>
          <i class="fa-solid fa-paper-plane"></i>
        </button>
      </div>
    </section>

    <section class="footer-section">
      <FooterSection />
    </section>
  </div>


  <!-- Import the FlightModal component -->
  <FlightModal :is-open="isFlightModalOpen" @close="closeFlightModal" />
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import FlightModal from '@/components/FlightModel.vue';
import FooterSection from '@/components/FooterSection.vue';
import LoginModal from '@/components/LoginModal.vue';
import RegisterModal from '@/components/RegisterModal.vue';
const showRegisterModal = ref(false);
const showLoginModal = ref(false);
const isFlightModalOpen = ref(false);
const switchToLogin = () => {
  showRegisterModal.value = false;
  showLoginModal.value = true;
};
const router = useRouter();
const openFlightModal = () => {
  isFlightModalOpen.value = true;
  document.body.style.overflow = 'hidden'; // Prevent background scrolling
};
const goToAbout = () => {
  router.push('/about');
}
const closeFlightModal = () => {
  isFlightModalOpen.value = false;
  document.body.style.overflow = ''; // Re-enable scrolling
};

// For parallax effect
let scene;

onMounted(() => {
  // Load AOS (Animate On Scroll) library
  if (typeof AOS !== 'undefined') {
    AOS.init({
      duration: 1000,
      once: true,
      offset: 100
    });
  }

  // Initialize parallax effect
  if (typeof Parallax !== 'undefined') {
    const sceneElement = document.getElementById('scene');
    if (sceneElement) {
      scene = new Parallax(sceneElement);
    }
  }

  // Mouse move effects for hero section
  const heroSection = document.querySelector('.hero-section');
  if (heroSection) {
    heroSection.addEventListener('mousemove', moveGradientOrbs);
  }
});

onUnmounted(() => {
  // Clean up
  if (scene) {
    scene.destroy();
  }
  const heroSection = document.querySelector('.hero-section');
  if (heroSection) {
    heroSection.removeEventListener('mousemove', moveGradientOrbs);
  }
});

// Function to move gradient orbs with mouse
const moveGradientOrbs = (e) => {
  const orbs = document.querySelectorAll('.orb');
  const mouseX = e.clientX / window.innerWidth;
  const mouseY = e.clientY / window.innerHeight;

  orbs.forEach((orb, index) => {
    const factor = (index + 1) * 20;
    const x = (mouseX * factor) - (factor / 2);
    const y = (mouseY * factor) - (factor / 2);

    orb.style.transform = `translate(${x}px, ${y}px)`;
  });
};
</script>

<style lang="scss" scoped>
// Enhanced color variables with more vibrant palette
$primary: #4361ee;
$primary-light: #4cc9f0;
$primary-dark: #3a0ca3;
$primary-color:#a3d2ca;
$primary-hover:#a3d2ca;
$accent: #f72585;
$accent-light1: #ffdae3;
$accent-light: #ff85a1;
$accent-secondary: #7209b7;
$gradient-start: #4cc9f0;
$gradient-mid: #4361ee;
$gradient-end: #3a0ca3;
$text: #2b2d42;
$text-light: #8d99ae;
$border: #edf2f4;
$shadow: rgba(67, 97, 238, 0.15);
$glass-bg: rgba(255, 255, 255, 0.6);


// Base styles
.auth-buttons {
  position: fixed;
  top: 20px; /* 设置按钮顶部距离 */
  right: 20px; /* 设置按钮右侧距离 */
  display: flex;
  flex-direction: column;
  gap: 15px; /* 按钮之间的间距 */
  z-index: 1000; /* 确保按钮位于页面最上层 */
  font-family: 'Poppins', sans-serif;

  .auth-button {
    padding: 12px 24px; /* 按钮内边距 */
    border: none;
    border-radius: 50px; /* 按钮圆角 */
    font-weight: 600;
    font-size: 1.4rem;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1); /* 平滑过渡动画 */
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
    text-transform: uppercase;
    width: 150px; /* 按钮宽度 */
    text-align: center;

    &:focus {
      outline: none; /* 去除聚焦时的默认边框 */
    }

    &:hover {
      transform: translateY(-3px); /* 悬停时按钮轻微上浮 */
      box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15); /* 悬停时阴影加深 */
    }

    &.login-button {
      background: linear-gradient(135deg, #a3d2ca, #5eaaa8); /* 登录按钮渐变背景 */
      color: white;

      &:hover {
        background: linear-gradient(135deg, #5eaaa8, #a3d2ca); /* 悬停时改变渐变方向 */
      }
    }

    &.register-button {
      background: white; /* 注册按钮背景 */
      color: #5eaaa8; /* 注册按钮文本颜色 */
      border: 2px solid #a3d2ca; /* 注册按钮边框 */

      &:hover {
        background-color: #e9ecef; /* 注册按钮悬停时背景色 */
        border-color: #5eaaa8; /* 注册按钮悬停时边框颜色 */
        color: #5eaaa8; /* 注册按钮悬停时文本颜色 */
      }
    }

    &.about-button {
      background: linear-gradient(135deg, #a3d2ca, #5eaaa8); /* About 按钮渐变背景 */
      color: white;

      &:hover {
        background: linear-gradient(135deg, #5eaaa8, #a3d2ca); /* 悬停时改变渐变方向 */
      }
    }
  }


  @media (max-width: 768px) {
    top: 15px;
    right: 15px;
    flex-direction: row; /* 在移动设备上按钮排成一行 */
    gap: 10px;

    .auth-button {
      padding: 10px 18px;
      font-size: 1.2rem; /* 在小屏设备上减小按钮大小 */
    }
  }
}

.home-view {
  padding-bottom: 5rem;
  overflow-x: hidden;
  font-family: 'Poppins', sans-serif;
  color: $text;
  background-color: #fafafa;
}

// Global section styling
section {
  padding: 3rem 2rem;
  margin: 0 auto;
  max-width: 1300px;
  margin-bottom: 8rem;
  position: relative;

  @media (min-width: 768px) {
    padding: 5rem 3rem;
  }
}

.section-header {
  text-align: center;
  margin-bottom: 5rem;

  .section-tag {
    display: inline-block;
    background: linear-gradient(135deg, $gradient-start, $gradient-end);
    color: white;
    padding: 0.6rem 1.2rem;
    border-radius: 30px;
    font-size: 1.2rem;
    font-weight: 600;
    letter-spacing: 1px;
    margin-bottom: 1.5rem;
    box-shadow: 0 10px 20px rgba($primary, 0.2);
  }

  h2 {
    color: $text;
    font-size: 3.5rem;
    font-weight: 800;
    margin-bottom: 1.5rem;
    background: linear-gradient(135deg, $gradient-start, $gradient-end);
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;

    @media (min-width: 768px) {
      font-size: 4.2rem;
    }
  }

  p {
    color: $text-light;
    font-size: 1.7rem;
    max-width: 650px;
    margin: 0 auto;
    line-height: 1.5;

    @media (min-width: 768px) {
      font-size: 1.9rem;
    }
  }
}

// Hero Section with animated elements
.hero-section {
  display: flex;
  flex-direction: column;
  padding-top: 6rem;
  position: relative;
  overflow: visible;
  min-height: 100vh;

  @media (min-width: 992px) {
    flex-direction: row;
    align-items: center;
    padding-top: 0;
  }
}

.animated-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba($gradient-start, 0.05), rgba($gradient-end, 0.05));
  z-index: -2;
  
  &::before {
    content: '';
    position: absolute;
    width: 100%;
    height: 100%;
    background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%234361ee' fill-opacity='0.05'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  }
}

.gradient-orbs {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: -1;
  
  .orb {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    opacity: 0.5;
    transition: transform 0.5s ease-out;
    
    &.orb-1 {
      top: 10%;
      left: 15%;
      width: 40rem;
      height: 40rem;
      background: radial-gradient(circle, rgba($primary-light, 0.4) 0%, rgba($primary-light, 0) 70%);
    }
    
    &.orb-2 {
      bottom: 20%;
      right: 10%;
      width: 50rem;
      height: 50rem;
      background: radial-gradient(circle, rgba($accent, 0.3) 0%, rgba($accent, 0) 70%);
    }
    
    &.orb-3 {
      top: 40%;
      right: 30%;
      width: 30rem;
      height: 30rem;
      background: radial-gradient(circle, rgba($accent-secondary, 0.3) 0%, rgba($accent-secondary, 0) 70%);
    }
  }
}

.hero-content {
  flex: 1;
  padding: 3rem 0;
  z-index: 1;
  position: relative;

  @media (min-width: 992px) {
    padding-right: 4rem;
  }
}

.hero-title {
  font-size: 4.5rem;
  font-weight: 900;
  margin-bottom: 2rem;
  line-height: 1.1;
  display: flex;
  flex-direction: column;
  
  .line {
    display: block;
    transform: translateY(20px);
    opacity: 0;
    animation: fadeInUp 0.8s forwards;
    
    &:nth-child(2) {
      animation-delay: 0.2s;
    }
    
    &:nth-child(3) {
      animation-delay: 0.4s;
    }
  }
  
  .accent-text {
    background: linear-gradient(135deg, $primary-light, $primary-dark);
    -webkit-background-clip: text;
    background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  @media (min-width: 768px) {
    font-size: 5.5rem;
  }

  @media (min-width: 992px) {
    font-size: 6.5rem;
  }
}

@keyframes fadeInUp {
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.hero-subtitle {
  font-size: 2rem;
  color: $text-light;
  margin-bottom: 3.5rem;
  max-width: 500px;
  opacity: 0;
  animation: fadeIn 0.8s 0.6s forwards;

  @media (min-width: 768px) {
    font-size: 2.2rem;
  }
}

@keyframes fadeIn {
  to {
    opacity: 1;
  }
}

.hero-cta {
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
  opacity: 0;
  animation: fadeIn 0.8s 0.8s forwards;

  button {
    padding: 1.5rem 3rem;
    border-radius: 50px;
    font-size: 1.7rem;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    gap: 1rem;

    i {
      transition: transform 0.3s ease;
    }

    &.primary-btn {
      background: linear-gradient(135deg, $accent-light1, $accent-light);
      color: white;
      border: none;
      box-shadow: 0 8px 20px rgba($accent, 0.4);

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 12px 25px rgba($accent, 0.5);
        
        i {
          transform: translateX(5px);
        }
      }

      &:active {
        transform: translateY(-2px);
      }
    }

    &.secondary-btn {
      background: rgba(255, 255, 255, 0.8);
      color: $accent-secondary;
      border: 2px solid rgba($accent-secondary, 0.3);
      backdrop-filter: blur(10px);

      &:hover {
        background: rgba(255, 255, 255, 0.95);
        transform: translateY(-5px);
        border-color: $accent-secondary;
        
        i {
          transform: translateX(5px);
        }
      }

      &:active {
        transform: translateY(-2px);
      }
    }
  }
}

.hero-image-container {
  flex: 1;
  position: relative;
  height: 50rem;
  margin-top: 5rem;
  z-index: 2;

  @media (min-width: 992px) {
    height: 60rem;
    margin-top: 0;
  }
}

.hero-image {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/pic/HeroImage.avif');
  background-size: cover;
  background-position: center;
  border-radius: 30px;
  box-shadow: 0 30px 60px rgba($primary-dark, 0.2);
  overflow: hidden;
  transform: rotate3d(1, 1, 1, 5deg);
  transition: transform 0.5s ease;
  
  &:hover {
    transform: rotate3d(1, 1, 1, 0deg);
  }

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(to bottom right, rgba($primary-dark, 0.2), rgba($accent, 0.2));
    border-radius: 30px;
  }
  
  .parallax-effect {
    position: absolute;
    width: 100%;
    height: 100%;
    
    &::before {
      content: '';
      position: absolute;
      inset: 0;
      background: url('https://images.unsplash.com/photo-1600859747268-54c7e693c327?q=80&w=2127&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D');
      background-size: cover;
      background-position: center;
    }
  }
}

.floating-card {
  position: absolute;
  background: $glass-bg;
  backdrop-filter: blur(10px);
  border-radius: 16px;
  padding: 1.8rem;
  display: flex;
  align-items: center;
  gap: 1.5rem;
  box-shadow: 0 15px 35px rgba($text, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  z-index: 10;
  transform: translateY(20px);
  opacity: 0;
  animation: floatIn 0.8s forwards;

  .card-icon {
    width: 4rem;
    height: 4rem;
    border-radius: 12px;
    background: linear-gradient(135deg, $primary, $primary-dark);
    display: flex;
    align-items: center;
    justify-content: center;
    
    i {
      font-size: 2rem;
      color: white;
    }
  }

  span {
    font-size: 1.6rem;
    font-weight: 600;
    color: $text;
  }

  &.floating-card-1 {
    top: 15%;
    left: -2rem;
    animation-delay: 1s;

    @media (min-width: 992px) {
      left: -5rem;
    }
  }

  &.floating-card-2 {
    bottom: 30%;
    right: -2rem;
    animation-delay: 1.3s;

    @media (min-width: 992px) {
      right: -3rem;
    }
  }
  
  &.floating-card-3 {
    bottom: 10%;
    left: 10%;
    animation-delay: 1.6s;
    
    @media (min-width: 992px) {
      left: 5%;
    }
  }
}

@keyframes floatIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// Services Section with flashy cards
.services-section {
  position: relative;
}
.service-cards {
  display: grid;
  grid-template-columns: 1fr;
  gap: 3rem;
  @media (min-width: 768px) {
    grid-template-columns: repeat(2, 1fr);
  }
  @media (min-width: 992px) {
    grid-template-columns: repeat(3, 1fr);
  }
}
.service-card {
  background: white;
  border-radius: 20px;
  padding: 4rem 2.5rem;
  box-shadow: 0 15px 40px $shadow;
  transition: all 0.5s cubic-bezier(0.23, 1, 0.32, 1);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  z-index: 1;
  
  .card-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: -1;
    background: linear-gradient(135deg, rgba($primary-light, 0.05), rgba($primary, 0.05));
    opacity: 0;
    transition: opacity 0.5s ease;
  }
  &:hover {
    transform: translateY(-15px) scale(1.03);
    box-shadow: 0 20px 50px rgba($primary-dark, 0.25);
    .card-bg {
      opacity: 1;
    }
    .card-icon-wrapper {
      background: linear-gradient(135deg, $primary, $accent-secondary);
      i {
        color: white;
      }
    }
    .card-cta {
      color: $primary-dark;
      i {
        transform: translateX(8px);
      }
    }
  }
  &:focus {
    outline: none;
    box-shadow: 0 0 0 3px $primary-light, 0 20px 50px rgba($primary-dark, 0.25);
    transform: translateY(-15px) scale(1.03);
  }
  h3 {
    font-size: 2.4rem;
    color: $text;
    margin: 2rem 0 1.5rem;
    font-weight: 700;
  }
  p {
    font-size: 1.6rem;
    color: $text-light;
    margin-bottom: 2.5rem;
    flex-grow: 1;
    line-height: 1.6;
  }
  
  /* Flight-specific animation */
  &:nth-child(1) {
    &:hover .card-icon-wrapper {
      transform: scale(1.1);
      i {
        animation: flight-animation 2s infinite linear;
      }
    }
  }
  
  /* Train-specific animation */
  &:nth-child(2) {
    &:hover .card-icon-wrapper {
      transform: scale(1.1);
      i {
        animation: train-animation 3s infinite linear;
      }
    }
  }
  
  /* Hotel-specific animation */
  &:nth-child(3) {
    &:hover .card-icon-wrapper {
      transform: scale(1.1);
      i {
        animation: hotel-animation 2s infinite ease;
      }
    }
  }
}

.card-icon-wrapper {
  width: 10rem;
  height: 10rem;
  border-radius: 50%;
  background: rgba($primary-light, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  margin-bottom: 0.5rem;
  box-shadow: 0 10px 20px rgba($primary, 0.1);
  i {
    font-size: 4rem;
    color: $primary;
    transition: all 0.5s ease;
  }
}

.card-cta {
  font-size: 1.6rem;
  font-weight: 600;
  color: $primary;
  display: flex;
  align-items: center;
  gap: 0.8rem;
  transition: all 0.3s ease;
  i {
    transition: transform 0.3s ease;
  }
}

/* Custom animations for each icon */
@keyframes flight-animation {
  0% {
    transform: translateY(0) rotate(0);
  }
  25% {
    transform: translateY(-5px) rotate(10deg);
  }
  50% {
    transform: translateY(0) rotate(0);
  }
  75% {
    transform: translateY(5px) rotate(-10deg);
  }
  100% {
    transform: translateY(0) rotate(0);
  }
}

@keyframes train-animation {
  0% {
    transform: translateX(-5px);
  }
  50% {
    transform: translateX(5px);
  }
  100% {
    transform: translateX(-5px);
  }
}

@keyframes hotel-animation {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.8;
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

// Features Section with glass morphism
.features-section {
  background: transparent;
  border-radius: 30px;
  padding: 8rem 3rem;
  position: relative;
  overflow: hidden;
}

.features-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;
  
  .blob {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    
    &.blob-1 {
      width: 60rem;
      height: 60rem;
      background: radial-gradient(circle, rgba($primary-light, 0.2) 0%, rgba($primary-light, 0) 70%);
      top: -20%;
      left: -10%;
    }
    
    &.blob-2 {
      width: 50rem;
      height: 50rem;
      background: radial-gradient(circle, rgba($accent, 0.15) 0%, rgba($accent, 0) 70%);
      bottom: -10%;
      right: -5%;
    }
  }
}

// Call-to-action Section
.cta-section {
  position: relative;
  background: linear-gradient(135deg, $primary-light, $primary, $primary-dark);
  border-radius: 30px;
  padding: 8rem 3rem;
  text-align: center;
  overflow: hidden;
  margin-bottom: 0;
}

.cta-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
  z-index: 0;
  
  .cta-blob {
    position: absolute;
    width: 100rem;
    height: 100rem;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.2) 0%, rgba(255, 255, 255, 0) 70%);
    top: -50%;
    right: -20%;
    filter: blur(50px);
  }
  
  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M11 18c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm48 25c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm-43-7c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm63 31c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM34 90c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm56-76c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM12 86c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm28-65c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm23-11c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-6 60c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm29 22c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zM32 63c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm57-13c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-9-21c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM60 91c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM35 41c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM12 60c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z' fill='%23ffffff' fill-opacity='0.1' fill-rule='evenodd'/%3E%3C/svg%3E");
  }
}

.cta-content {
  position: relative;
  z-index: 1;
  color: white;
  max-width: 800px;
  margin: 0 auto;
  
  h2 {
    font-size: 4rem;
    font-weight: 800;
    margin-bottom: 2rem;
    
    @media (min-width: 768px) {
      font-size: 4.5rem;
    }
  }
  
  p {
    font-size: 1.8rem;
    opacity: 0.9;
    margin-bottom: 3rem;
    max-width: 600px;
    margin-left: auto;
    margin-right: auto;
    
    @media (min-width: 768px) {
      font-size: 2rem;
    }
  }
}

.cta-button {
  background: white;
  color: $primary-dark;
  border: none;
  border-radius: 50px;
  padding: 1.5rem 3.5rem;
  font-size: 1.8rem;
  font-weight: 700;
  display: inline-flex;
  align-items: center;
  gap: 1rem;
  cursor: pointer;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  
  i {
    transition: transform 0.3s ease;
  }
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
    
    i {
      transform: translateX(5px);
    }
  }
  
  &:active {
    transform: translateY(-2px);
  }
}

// Animations and Additional Effects
@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

// Custom scrollbar
::-webkit-scrollbar {
  width: 12px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

::-webkit-scrollbar-thumb {
  background: linear-gradient(to bottom, $primary-light, $primary);
  border-radius: 10px;
  border: 3px solid #f1f1f1;
}

::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(to bottom, $primary, $primary-dark);
}

.testimonials-section {
  padding-top: 6rem;
}

.testimonials-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 3rem;

  @media (min-width: 768px) {
    grid-template-columns: repeat(2, 1fr);
  }
}

.testimonial-card {
  background: white;
  border-radius: 24px;
  padding: 4rem 3rem;
  box-shadow: 0 20px 40px $shadow;
  position: relative;
  transition: transform 0.4s ease, box-shadow 0.4s ease;
  
  &:hover {
    transform: translateY(-10px);
    box-shadow: 0 30px 50px rgba($primary-dark, 0.2);
  }
}

.testimonial-quote-icon {
  position: absolute;
  top: -20px;
  left: 30px;
  width: 4.5rem;
  height: 4.5rem;
  border-radius: 50%;
  background: linear-gradient(135deg, $primary, $accent-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
  
  i {
    font-size: 2rem;
    color: white;
  }
}

.testimonial-rating {
  margin-bottom: 2rem;
  display: flex;
  gap: 0.3rem;

  i {
    color: #ffb74d;
    font-size: 2rem;
  }
}

.testimonial-text {
  font-size: 1.8rem;
  color: $text;
  line-height: 1.7;
  font-style: italic;
  margin-bottom: 3rem;
}

.testimonial-author {
  display: flex;
  align-items: center;
  gap: 1.5rem;

  .author-avatar {
    width: 6rem;
    height: 6rem;
    border-radius: 50%;
    background: #eee;
    background-size: cover;
    background-position: center;
    border: 3px solid white;
    box-shadow: 0 5px 15px rgba($text, 0.1);

    &.auther-1 {
      background-image: url('https://randomuser.me/api/portraits/women/44.jpg');
    }

    &.auther-2 {
      background-image: url('https://randomuser.me/api/portraits/men/32.jpg');
    }
  }

  .author-info {
    h4 {
      font-size: 1.8rem;
      font-weight: 700;
      color: $text;
      margin-bottom: 0.4rem;
    }

    p {
      font-size: 1.5rem;
      color: $text-light;
      font-weight: 500;
    }
  }
}

.testimonial-badge {
  position: absolute;
  top: 30px;
  right: 30px;
  background: rgba($primary, 0.1);
  color: $primary;
  font-size: 1.4rem;
  font-weight: 600;
  padding: 0.6rem 1.2rem;
  border-radius: 30px;
}.features-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 3rem;

  @media (min-width: 768px) {
    grid-template-columns: repeat(2, 1fr);
  }
}

.feature-item {
  display: flex;
  gap: 2.5rem;
  padding: 2.5rem;
  border-radius: 20px;
  transition: transform 0.4s ease, box-shadow 0.4s ease;
  
  &.glassmorphism {
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(20px);
    border: 1px solid rgba(255, 255, 255, 0.3);
    box-shadow: 0 10px 30px rgba($primary-dark, 0.1);
  }
  
  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 15px 40px rgba($primary-dark, 0.15);
  }

  .feature-icon {
    flex-shrink: 0;
    width: 7rem;
    height: 7rem;
    border-radius: 20px;
    background: linear-gradient(135deg, $primary, $accent-secondary);
    box-shadow: 0 10px 20px rgba($primary-dark, 0.2);
    display: flex;
    align-items: center;
    justify-content: center;
    transition: transform 0.4s ease;

    i {
      font-size: 2.8rem;
      color: white;
    }
  }
  
  &:hover .feature-icon {
    transform: rotate(10deg);
  }

  .feature-content {
    h3 {
      font-size: 2.2rem;
      color: $text;
      margin-bottom: 1rem;
      font-weight: 700;
    }

    p {
      font-size: 1.6rem;
      color: $text-light;
      line-height: 1.6;
    }
  }
}

.footer-section {
  margin: 0 auto;
  padding-bottom: 2rem;
  border-radius: 20px;
}
</style>