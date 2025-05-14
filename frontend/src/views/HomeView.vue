<template>
  <div class="home-view">
    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">Discover the World with SwiftTravel</h1>
        <p class="hero-subtitle">Your journey begins with a single click</p>
        <div class="hero-cta">
          <button @click="$router.push('/search?form=Train')" class="primary-btn">Book Trains</button>
          <button @click="$router.push('/search?form=Hotel')" class="secondary-btn">Book Hotel</button>
        </div>
      </div>
      <div class="hero-image-container">
        <div class="hero-image"></div>
        <div class="floating-card floating-card-1">
          <i class="fa-solid fa-map-location-dot"></i>
          <span>1200+ Destinations</span>
        </div>
        <div class="floating-card floating-card-2">
          <i class="fa-solid fa-users"></i>
          <span>10M+ Happy Travelers</span>
        </div>
      </div>
    </section>

    <!-- Services Section -->
    <section class="services-section">
      <div class="section-header">
        <h2>Travel Your Way</h2>
        <p>Seamless booking experience for all your travel needs</p>
      </div>
      
      <div class="service-cards">
        <div class="service-card" tabindex="0" @click="openFlightModal">
          <div class="card-icon-wrapper">
            <i class="fa-solid fa-plane"></i>
          </div>
          <h3>Flights</h3>
          <p>Find and book the best deals on flights worldwide with our price matching guarantee</p>
          <span class="card-cta">Search Now <i class="fa-solid fa-arrow-right"></i></span>
        </div>
        
        <div class="service-card" tabindex="0" @click="$router.push('/search?form=Train')">
          <div class="card-icon-wrapper">
            <i class="fa-solid fa-train"></i>
          </div>
          <h3>Trains</h3>
          <p>Book train tickets with meal service options and enjoy a comfortable journey</p>
          <span class="card-cta">Book Now <i class="fa-solid fa-arrow-right"></i></span>
        </div>
        
        <div class="service-card" tabindex="0" @click="$router.push('/search?form=Hotel')">
          <div class="card-icon-wrapper">
            <i class="fa-solid fa-hotel"></i>
          </div>
          <h3>Hotels</h3>
          <p>Discover comfortable stays at great prices with our curated selection of hotels</p>
          <span class="card-cta">Find Hotels <i class="fa-solid fa-arrow-right"></i></span>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features-section">
      <div class="section-header">
        <h2>Why Choose SwiftTravel</h2>
        <p>We make your travel planning simple, efficient and enjoyable</p>
      </div>
      
      <div class="features-grid">
        <div class="feature-item">
          <div class="feature-icon">
            <i class="fa-solid fa-shield-halved"></i>
          </div>
          <div class="feature-content">
            <h3>Secure Booking</h3>
            <p>Your personal and payment information is always protected</p>
          </div>
        </div>
        
        <div class="feature-item">
          <div class="feature-icon">
            <i class="fa-solid fa-tag"></i>
          </div>
          <div class="feature-content">
            <h3>Best Price Guarantee</h3>
            <p>Find a lower price elsewhere? We'll match it</p>
          </div>
        </div>
        
        <div class="feature-item">
          <div class="feature-icon">
            <i class="fa-solid fa-headset"></i>
          </div>
          <div class="feature-content">
            <h3>24/7 Support</h3>
            <p>Our support team is always available to help you</p>
          </div>
        </div>
        
        <div class="feature-item">
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

    <!-- Testimonials Section -->
    <section class="testimonials-section">
      <div class="section-header">
        <h2>What Our Customers Say</h2>
        <p>Join thousands of satisfied travelers</p>
      </div>
      
      <div class="testimonials-container">
        <div class="testimonial-card">
          <div class="testimonial-rating">
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
          </div>
          <p class="testimonial-text">"The train meal ordering service saved my trip! Being able to select my food before boarding made the journey so much more enjoyable."</p>
          <div class="testimonial-author">
            <div class="author-avatar"></div>
            <div class="author-info">
              <h4>Sarah J.</h4>
              <p>Business Traveler</p>
            </div>
          </div>
        </div>
        
        <div class="testimonial-card">
          <div class="testimonial-rating">
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
            <i class="fa-solid fa-star"></i>
          </div>
          <p class="testimonial-text">"Found a great hotel deal that was cheaper than anywhere else. The booking process was smooth and customer service excellent!"</p>
          <div class="testimonial-author">
            <div class="author-avatar"></div>
            <div class="author-info">
              <h4>Michael T.</h4>
              <p>Family Vacationer</p>
            </div>
          </div>
        </div>
      </div>
    </section>

  </div>

  <!-- Import the FlightModal component -->
  <FlightModal :is-open="isFlightModalOpen" @close="closeFlightModal" />
</template>

<script setup>
import { ref } from 'vue';
import FlightModal from '@/components/FlightModel.vue';

const isFlightModalOpen = ref(false);

const openFlightModal = () => {
  isFlightModalOpen.value = true;
  document.body.style.overflow = 'hidden'; // Prevent background scrolling
};

const closeFlightModal = () => {
  isFlightModalOpen.value = false;
  document.body.style.overflow = ''; // Re-enable scrolling
};
</script>

<style lang="scss" scoped>
// Color variables (matching existing design)
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;
$border: #e6eaf0;
$shadow: rgba(50, 112, 233, 0.08);

// For gradients
$primary-extra-light: #a8c7ff;
$accent-color: #36d6e7;
$accent-light: #5de1ee;
$highlight: #8c5eff;

// Base styles
.home-view {
  padding-bottom: 5rem;
  overflow-x: hidden;
}

// Global section styling
section {
  padding: 2rem;
  margin: 0 auto;
  max-width: 1200px;
  margin-bottom: 5rem;

  @media (min-width: 768px) {
    padding: 3rem;
  }
}

.section-header {
  text-align: center;
  margin-bottom: 4rem;
  
  h2 {
    color: $text;
    font-size: 3.2rem;
    font-weight: 700;
    margin-bottom: 1rem;
    
    @media (min-width: 768px) {
      font-size: 3.6rem;
    }
  }
  
  p {
    color: $text-light;
    font-size: 1.6rem;
    max-width: 600px;
    margin: 0 auto;
    
    @media (min-width: 768px) {
      font-size: 1.8rem;
    }
  }
}

// Hero Section
.hero-section {
  display: flex;
  flex-direction: column;
  padding-top: 4rem;
  position: relative;
  overflow: hidden;
  
  @media (min-width: 992px) {
    flex-direction: row;
    align-items: center;
    min-height: 80vh;
    padding-top: 0;
  }
  
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    right: -20%;
    width: 80rem;
    height: 80rem;
    border-radius: 50%;
    background: radial-gradient(circle, rgba($primary-light, 0.08) 0%, rgba($primary-light, 0) 70%);
    z-index: -1;
  }
  
  &::after {
    content: '';
    position: absolute;
    bottom: -30%;
    left: -10%;
    width: 60rem;
    height: 60rem;
    border-radius: 50%;
    background: radial-gradient(circle, rgba($accent-color, 0.08) 0%, rgba($accent-color, 0) 70%);
    z-index: -1;
  }
}

.hero-content {
  flex: 1;
  padding: 2rem 0;
  z-index: 1;
  
  @media (min-width: 992px) {
    padding-right: 4rem;
  }
}

.hero-title {
  font-size: 4rem;
  font-weight: 800;
  margin-bottom: 1.5rem;
  line-height: 1.2;
  background: linear-gradient(135deg, $primary-dark, $primary, $accent-color);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  
  @media (min-width: 768px) {
    font-size: 5rem;
  }
  
  @media (min-width: 992px) {
    font-size: 5.6rem;
  }
}

.hero-subtitle {
  font-size: 1.8rem;
  color: $text-light;
  margin-bottom: 3rem;
  max-width: 500px;
  
  @media (min-width: 768px) {
    font-size: 2rem;
  }
}

.hero-cta {
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap;
  
  button {
    padding: 1.2rem 2.4rem;
    border-radius: 8px;
    font-size: 1.6rem;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &.primary-btn {
      background: linear-gradient(135deg, $primary, $primary-dark);
      color: white;
      border: none;
      box-shadow: 0 4px 12px rgba($primary-dark, 0.3);
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba($primary-dark, 0.4);
      }
      
      &:active {
        transform: translateY(0);
      }
    }
    
    &.secondary-btn {
      background: white;
      color: $primary;
      border: 2px solid $primary-light;
      
      &:hover {
        background: rgba($primary-light, 0.1);
        transform: translateY(-2px);
      }
      
      &:active {
        transform: translateY(0);
      }
    }
  }
}

.hero-image-container {
  flex: 1;
  position: relative;
  height: 40rem;
  margin-top: 3rem;
  
  @media (min-width: 992px) {
    height: 50rem;
    margin-top: 0;
  }
}

.hero-image {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image: url('https://images.unsplash.com/photo-1436491865332-7a61a109cc05?ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80');
  background-size: cover;
  background-position: center;
  border-radius: 24px;
  box-shadow: 0 20px 40px rgba($primary-dark, 0.15);
  
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(to bottom right, rgba($primary-dark, 0.2), rgba($accent-color, 0.2));
    border-radius: 24px;
  }
}

.floating-card {
  position: absolute;
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  box-shadow: 0 10px 25px rgba($text, 0.1);
  animation: float 6s ease-in-out infinite;
  
  i {
    font-size: 2.2rem;
    color: $primary;
  }
  
  span {
    font-size: 1.4rem;
    font-weight: 600;
    color: $text;
  }
  
  &.floating-card-1 {
    top: 15%;
    left: -2rem;
    animation-delay: 0s;
    z-index: 2;
    
    @media (min-width: 992px) {
      left: -5rem;
    }
  }
  
  &.floating-card-2 {
    bottom: 15%;
    right: -2rem;
    animation-delay: 3s;
    z-index: 2;
    
    @media (min-width: 992px) {
      right: -5rem;
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-15px);
  }
}

// Services Section
.services-section {
  position: relative;
}

.service-cards {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2.5rem;
  
  @media (min-width: 768px) {
    grid-template-columns: repeat(2, 1fr);
  }
  
  @media (min-width: 992px) {
    grid-template-columns: repeat(3, 1fr);
  }
}

.service-card {
  background: white;
  border-radius: 16px;
  padding: 3rem 2rem;
  box-shadow: 0 8px 30px $shadow;
  transition: all 0.4s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 4px;
    background: linear-gradient(to right, $primary, $accent-color);
    transform: scaleX(0);
    transform-origin: left;
    transition: transform 0.4s ease;
  }
  
  &:hover {
    transform: translateY(-8px);
    box-shadow: 0 15px 35px rgba($primary-dark, 0.15);
    
    &::before {
      transform: scaleX(1);
    }
    
    .card-icon-wrapper {
      background: linear-gradient(135deg, $primary, $primary-light);
      
      i {
        color: white;
      }
    }
    
    .card-cta {
      color: $primary-dark;
      
      i {
        transform: translateX(5px);
      }
    }
  }
  
  &:focus {
    outline: none;
    box-shadow: 0 0 0 3px $primary-light, 0 15px 35px rgba($primary-dark, 0.15);
    transform: translateY(-8px);
    
    &::before {
      transform: scaleX(1);
    }
  }
  
  h3 {
    font-size: 2.2rem;
    color: $text;
    margin: 1.5rem 0 1rem;
    font-weight: 700;
  }
  
  p {
    font-size: 1.5rem;
    color: $text-light;
    margin-bottom: 2rem;
    flex-grow: 1;
  }
}

.card-icon-wrapper {
  width: 8rem;
  height: 8rem;
  border-radius: 50%;
  background: rgba($primary-light, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  margin-bottom: 0.5rem;
  
  i {
    font-size: 3.2rem;
    color: $primary;
    transition: all 0.3s ease;
  }
}

.card-cta {
  font-size: 1.5rem;
  font-weight: 600;
  color: $primary;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
  
  i {
    transition: transform 0.3s ease;
  }
}

// Features Section
.features-section {
  background: linear-gradient(135deg, rgba($primary-extra-light, 0.08), rgba($accent-light, 0.08));
  border-radius: 24px;
  padding: 5rem 3rem;
}

.features-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 3rem;
  
  @media (min-width: 768px) {
    grid-template-columns: repeat(2, 1fr);
  }
}

.feature-item {
  display: flex;
  gap: 2rem;
  
  .feature-icon {
    flex-shrink: 0;
    width: 6rem;
    height: 6rem;
    border-radius: 50%;
    background: white;
    box-shadow: 0 8px 16px rgba($primary-dark, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
    
    i {
      font-size: 2.4rem;
      color: $primary;
    }
  }
  
  .feature-content {
    h3 {
      font-size: 2rem;
      color: $text;
      margin-bottom: 0.5rem;
      font-weight: 600;
    }
    
    p {
      font-size: 1.5rem;
      color: $text-light;
    }
  }
}

// Testimonials Section
.testimonials-section {
  padding-top: 4rem;
}

.testimonials-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2.5rem;
  
  @media (min-width: 768px) {
    grid-template-columns: repeat(2, 1fr);
  }
}

.testimonial-card {
  background: white;
  border-radius: 16px;
  padding: 3rem;
  box-shadow: 0 10px 30px $shadow;
}

.testimonial-rating {
  margin-bottom: 1.5rem;
  
  i {
    color: #ffb74d;
    font-size: 1.8rem;
    margin-right: 0.3rem;
  }
}

.testimonial-text {
  font-size: 1.6rem;
  color: $text;
  line-height: 1.7;
  font-style: italic;
  margin-bottom: 2rem;
}

.testimonial-author {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  
  .author-avatar {
    width: 5rem;
    height: 5rem;
    border-radius: 50%;
    background: #eee;
    background-size: cover;
    background-position: center;
    
    &:nth-child(1) {
      background-image: url('https://randomuser.me/api/portraits/women/44.jpg');
    }
    
    &:nth-child(2) {
      background-image: url('https://randomuser.me/api/portraits/men/32.jpg');
    }
  }
  
  .author-info {
    h4 {
      font-size: 1.6rem;
      font-weight: 600;
      color: $text;
      margin-bottom: 0.2rem;
    }
    
    p {
      font-size: 1.4rem;
      color: $text-light;
    }
  }
}
</style>