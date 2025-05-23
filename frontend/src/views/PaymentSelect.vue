<template>
  <div class="payment-select-container">
    <h2 class="title">请选择支付方式</h2>

    <div class="order-summary">
      <p><strong>订单类型：</strong>{{ payTypeDisplay }}</p>
      <p><strong>订单金额：</strong>¥{{ amount.toFixed(2) }}</p>
    </div>

    <div class="payment-options">
      <div class="payment-card" @click="selectMethod('WECHAT')">
        <i class="fa-brands fa-weixin icon wechat"></i>
        <span>微信支付</span>
      </div>

      <div class="payment-card" @click="selectMethod('ALIPAY')">
        <i class="fa-brands fa-alipay icon alipay"></i>
        <span>支付宝支付</span>
      </div>

      <div class="payment-card" @click="selectMethod('BANK')">
        <i class="fa-solid fa-credit-card icon bank"></i>
        <span>银行卡支付</span>
      </div>
    </div>

    <button class="cancel-btn" @click="cancelPayment">取消支付</button>
  </div>
</template>

<script>
import api from "@/api/Api.js";

export default {
  name: 'PaymentSelect',
  props: ['orderId', 'payType', 'amount'],
  data() {
    return {
      paymentId: '', // 存储创建成功后的 paymentId
    };
  },
  computed: {
    payTypeDisplay() {
      const map = { TICKET: '车票', HOTEL: '酒店', FOOD: '餐食' };
      return map[this.payType] || this.payType;
    }
  },
  methods: {
    async selectMethod(method) {
      const payload = {
        orderId: this.orderId,
        payType: this.payType,
        amount: this.amount,
        method
      };

      try {
        const response = await api.post('/api/payment/create', payload);
        const result = response.data;

        if (result.code === 200) {
          this.paymentId = result.data;
          this.$router.push({
            name: 'PaymentSimulate',
            params: { paymentId: this.paymentId, method }
          });
        } else {
          alert('创建支付失败：' + result.message);
        }
      } catch (e) {
        alert('网络错误，请稍后重试');
      }
    },

    async cancelPayment() {
      const confirmCancel = confirm('确定取消支付？');
      if (!confirmCancel || !this.paymentId) return;

      try {
        const response = await api.post(`/api/payment/cancel/${this.paymentId}`);
        const result = response.data;

        if (result.code === 200) {
          alert('支付已取消');
        } else {
          alert('取消失败：' + result.message);
        }

        this.$router.push('/');
      } catch (e) {
        alert('网络异常，取消失败');
      }
    }
  }
};
</script>

<style scoped lang="scss">
$primary: #4a8eff;

.payment-select-container {
  max-width: 90%;
  width: 90%;
  height: 95%;
  margin: 3rem auto;
  padding: 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06);
  text-align: center;
}

.title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 2rem;
  margin-top: 5rem;
}

.order-summary {
  background: #f9f9f9;
  border-radius: 12px;
  padding: 3rem 2rem;
  margin: 3rem auto 7% auto;   // 顶部间距、底部留白、左右自动居中
  font-size: 2rem;
  color: #555;
  text-align: left;
  width: 80%;
  height: 14%;
}

.payment-options {
  display: flex;
  flex-direction: column; // 改为纵向排列
  gap: 4rem;
  align-items: center;    // 居中对齐
  margin-top: 5rem;          // ✅ 增加距离，让卡片更靠下
}

.payment-card {
  width: 70%;
  //flex: 1;
  min-width: 30%;
  background: #f0f4ff;
  border-radius: 12px;
  padding: 2rem 1rem;
  text-align: center;
  cursor: pointer;
  transition: 0.3s;
  border: 2px solid transparent;

  &:hover {
    transform: translateY(-4px);
    border-color: $primary;
    background: #e6f0ff;
  }

  .icon {
    font-size: 2.8rem;
    margin-bottom: 1rem;
  }

  .wechat {
    color: #1aad19;
  }

  .alipay {
    color: #0078ff;
  }

  .bank {
    color: #888;
  }

  span {
    font-size: 1.8rem;
    font-weight: 600;
    color: #333;
  }
}

.cancel-btn {
  margin-top: 3rem;
  background-color: #ccc;
  color: #333;
  border: none;
  padding: 1rem 2rem;
  border-radius: 8px;
  font-size: 1.4rem;
  cursor: pointer;
  transition: 0.3s;

  &:hover {
    background-color: #bbb;
  }
}

</style>
