<!-- FlightModal.vue -->
<template>
  <div class="modal-overlay" v-if="isOpen" @click="closeModal">
    <div class="modal-container" @click.stop>
      <div class="modal-content">
        <div class="modal-icon">
          <i class="fa-solid fa-plane"></i>
        </div>
        <h2 class="modal-title">Coming Soon!</h2>
        <p class="modal-text">Our flight booking service is currently under development. We're working hard to bring you the best flight deals soon!</p>
        <div class="modal-footer">
          <button class="modal-button" @click="closeModal">I Understand</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue';

const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true
  }
});

const emit = defineEmits(['close']);

const closeModal = () => {
  emit('close');
};

// Close modal with escape key
const handleKeydown = (e) => {
  if (e.key === 'Escape' && props.isOpen) {
    closeModal();
  }
};

// Add and remove event listener
onMounted(() => {
  window.addEventListener('keydown', handleKeydown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown);
});
</script>

<style lang="scss" scoped>
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;
$shadow: rgba(50, 112, 233, 0.08);

// For gradients
$primary-extra-light: #a8c7ff;
$accent-color: #36d6e7;
$accent-light: #5de1ee;
$highlight: #8c5eff;

// Modal Styles
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease;
}

.modal-container {
  width: 90%;
  max-width: 500px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  animation: slideIn 0.4s ease-out;
  transform-origin: center;
}

.modal-content {
  padding: 3rem;
  text-align: center;
}

.modal-icon {
  margin-bottom: 2rem;
  font-size: 6rem;
  height: 12rem;
  width: 12rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 2rem;
  background: linear-gradient(135deg, $primary-light, $primary-dark);
  border-radius: 50%;
  
  i {
    font-size: 6rem;
    color: white;
    animation: floatPlane 3s ease-in-out infinite;
  }
}

.modal-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  background: linear-gradient(90deg, $primary-dark, $highlight);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.modal-text {
  font-size: 1.6rem;
  color: $text-light;
  margin-bottom: 3rem;
  line-height: 1.8;
}

.modal-footer {
  display: flex;
  justify-content: center;
}

.modal-button {
  background: linear-gradient(135deg, $primary, $primary-dark);
  color: white;
  border: none;
  border-radius: 50px;
  padding: 1.2rem 3.5rem;
  font-size: 1.6rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 5px 15px rgba($primary, 0.4);
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 20px rgba($primary, 0.6);
    background: linear-gradient(135deg, $primary-light, $primary);
  }
  
  &:active {
    transform: translateY(0);
    box-shadow: 0 3px 10px rgba($primary, 0.4);
  }
}

// Animations
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: scale(0.9);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes floatPlane {
  0%, 100% {
    transform: translateY(0) rotate(5deg);
  }
  50% {
    transform: translateY(-10px) rotate(0deg);
  }
}

// Responsive adjustments
@media (max-width: 768px) {
  .modal-container {
    width: 95%;
    max-width: 450px;
  }
  
  .modal-icon {
    height: 10rem;
    width: 10rem;
    
    i {
      font-size: 5rem;
    }
  }
  
  .modal-title {
    font-size: 2.5rem;
  }
}
</style>