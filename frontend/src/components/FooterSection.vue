<script setup>
// 页脚组件
</script>

<template>
  <footer class="footer-section">
    <div class="footer-content">
      <div class="footer-left">
        <div class="footer-brand">
          <i class="fa-solid fa-dove"></i>
          <span>SwiftTravel</span>
        </div>
        <p class="copyright">© 2025 SwiftTravel 版权所有</p>
      </div>
      <div class="footer-right">
        <div class="footer-links">
          <a href="#">关于我们</a>
          <a href="#">使用条款</a>
          <a href="#">隐私政策</a>
          <a href="#">帮助中心</a>
        </div>
        <div class="social-links">
          <a href="#" title="微信"><i class="fab fa-weixin"></i></a>
          <a href="#" title="微博"><i class="fab fa-weibo"></i></a>
          <a href="#" title="QQ"><i class="fab fa-qq"></i></a>
        </div>
      </div>
    </div>
  </footer>
</template>

<style lang="scss" scoped>
// 颜色变量
$primary: #4a8eff;
$primary-light: #6ba3ff;
$primary-dark: #3270e9;
$text: #2c3e50;
$text-light: #5d7290;
$bg-dark: #f0f4fc;
$primary-gradient: linear-gradient(135deg, $primary-light, $primary-dark);

.footer-section {
  background-color: $bg-dark;
  padding: 3rem 0 2rem;
  margin-top: auto;

  .footer-content {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    padding: 0 2rem;
    flex-wrap: wrap;
    gap: 2rem;

    .footer-left {
      .footer-brand {
        display: flex;
        align-items: center;
        gap: 1rem;
        margin-bottom: 1rem;
        
        i {
          font-size: 2.4rem;
          background: $primary-gradient;
          background-clip: text;
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
        
        span {
          font-size: 2rem;
          font-weight: 700;
          color: $text;
        }
      }
      
      .copyright {
        font-size: 1.4rem;
        color: $text-light;
      }
    }

    .footer-right {
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
      
      .footer-links {
        display: flex;
        gap: 2rem;
        flex-wrap: wrap;

        a {
          font-size: 1.4rem;
          color: $text;
          text-decoration: none;
          transition: color 0.2s ease;

          &:hover {
            color: $primary-dark;
          }
        }
      }
      
      .social-links {
        display: flex;
        gap: 1.5rem;
        justify-content: flex-end;
        
        a {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 3.6rem;
          height: 3.6rem;
          border-radius: 50%;
          background-color: rgba($primary, 0.1);
          color: $primary;
          transition: all 0.2s ease;
          
          i {
            font-size: 1.8rem;
          }
          
          &:hover {
            background-color: $primary;
            color: white;
            transform: translateY(-3px);
          }
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .footer-section {
    .footer-content {
      flex-direction: column;
      text-align: center;
      
      .footer-left {
        .footer-brand {
          justify-content: center;
        }
      }
      
      .footer-right {
        width: 100%;
        
        .footer-links {
          justify-content: center;
        }
        
        .social-links {
          justify-content: center;
        }
      }
    }
  }
}
</style>