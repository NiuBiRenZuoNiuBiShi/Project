<!-- HomeView.vue -->
<template>
  <div class="content-area">
    <div class="content-wrapper">
      <h1>Welcome to SwiftTravel</h1>
      <div style="margin-bottom: 3rem;">
        <span>
          SwiftTravel is your one-stop solution for all travel needs. Whether you're looking for flights,
          train tickets, or hotel bookings, we've got you covered. Our platform is designed to make your travel
          planning as smooth and efficient as possible.
        </span>
      </div>
      <div class="card-grid">
        <div class="card" tabindex="0" @click="openFlightModal">
          <i class="fa-solid fa-plane"></i>
          <h3>Flights</h3>
          <p>Find the best deals on flights worldwide</p>
        </div>
        <div class="card" @click="$router.push('/search?form=Train')" tabindex="0">
          <i class="fa-solid fa-train"></i>
          <h3>Trains</h3>
          <p>Book train tickets for your journey</p>
        </div>
        <div class="card" @click="$router.push('/search?form=Hotel')" tabindex="0">
          <i class="fa-solid fa-hotel"></i>
          <h3>Hotels</h3>
          <p>Discover comfortable stays at great prices</p>
        </div>
      </div>
    </div>
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

.content-area {
  padding: 2rem;
  overflow-y: auto;
  flex-grow: 1;
}

.content-wrapper {
  padding-left: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  overflow: hidden;

  h1 {
    color: transparent;
    -webkit-text-fill-color: transparent;
    font-size: clamp(3.6rem, 6vw, 8rem);
    font-weight: 700;
    letter-spacing: -0.5px;
    position: relative;

    background-image:
      linear-gradient(135deg,
        rgba(255, 255, 255, 0.1) 0%,
        rgba(255, 255, 255, 0) 50%,
        rgba(0, 0, 0, 0.05) 100%),
      linear-gradient(90deg,
        $primary-dark,
        $primary,
        $primary-light,
        $accent-color,
        $accent-light,
        $primary-extra-light,
        $primary,
        $primary-light);

    background-clip: text;
    -webkit-background-clip: text;
    text-shadow:
      1px 1px 0 rgba(0, 0, 0, 0.1),
      3px 3px 5px $shadow;
    background-size: 300%;
    animation: headerColorRun 8s ease-in-out infinite alternate;
    transition: all 0.3s ease;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 50%;
      height: 100%;
      background: linear-gradient(to right,
          rgba(255, 255, 255, 0) 0%,
          rgba(255, 255, 255, 0.3) 50%,
          rgba(255, 255, 255, 0) 100%);
      transform: skewX(-25deg);
      animation: shine 8s infinite ease-in-out;
      animation-delay: 3s;
    }

    &:hover {
      transform: translateY(-2px) scale(1.01);
      text-shadow:
        1px 1px 0 rgba(0, 0, 0, 0.1),
        3px 3px 10px $shadow;
    }
  }

  span {
    color: $text-light;
    line-height: 1.6;
    margin-bottom: 3rem;
    font-size: 1.6rem;

    background: linear-gradient(to right, $primary, $highlight) no-repeat;
    background-size: 0 2px;
    background-position: right bottom;
    transition: background-size 1s ease;

    &:hover {
      background-size: 100% 2px;
      background-position: left bottom;
    }
  }
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;

  .card {
    background: white;
    border-radius: 12px;
    padding: 1.5rem;
    box-shadow: 0 4px 15px $shadow;
    transition: transform 0.3s ease, box-shadow 0.3s ease;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 20px $shadow;
      cursor: pointer;
    }

    &:focus {
      transform: translateY(-5px);
      outline: none;
      box-shadow: 0 0 0 4px $primary-light;
    }

    i {
      font-size: 3rem;
      color: $primary;
      margin-bottom: 1rem;
    }

    h3 {
      color: $text;
      margin-bottom: 0.75rem;
      font-size: 1.8rem;
      font-weight: 600;
    }

    p {
      color: $text-light;
      font-size: 1.2rem;
      margin-bottom: 0;
    }
  }
}

// Animations
@keyframes headerColorRun {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

@keyframes shine {
  0%,
  15% {
    left: -100%;
  }
  35%,
  100% {
    left: 200%;
  }
}
</style>