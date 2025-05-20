<template>
  <div class="simulate-container">

    <div v-if="method === 'WECHAT' || method === 'ALIPAY'" class="qr-box">
      <img :src="qrUrl" alt="支付二维码" class="qr-image" />
      <p>请使用 {{ paymentName }} 扫描二维码支付</p>
    </div>

    <div v-else-if="method === 'BANK'" class="form-box">
      <input v-model="cardNumber" placeholder="银行卡号" />
      <input v-model="password" placeholder="支付密码" type="password" />
    </div>

    <button class="confirm-btn" @click="completePayment">确认支付</button>
  </div>
</template>

<script>
export default {
  name: 'PaymentSimulate',
  props: ['paymentId', 'method'],
  data() {
    return {
      cardNumber: '',
      password: ''
    };
  },
  computed: {
    paymentName() {
      switch (this.method) {
        case 'WECHAT': return '微信';
        case 'ALIPAY': return '支付宝';
        case 'BANK': return '银行卡';
        default: return '支付';
      }
    },
    qrUrl() {
      return this.method === 'WECHAT'
          ? 'https://api.qrserver.com/v1/create-qr-code/?data=wechat-pay&size=180x180'
          : 'https://api.qrserver.com/v1/create-qr-code/?data=alipay-pay&size=180x180';
    }
  },
  methods: {
    async completePayment() {
      const res = await fetch(`/api/payment/complete/${this.paymentId}`, {
        method: 'POST'
      });
      const result = await res.json();
      if (result.code === 200) {
        alert('支付成功！');
        this.$router.push('/'); // 返回首页或支付成功页
      } else {
        alert('支付失败，请稍后再试');
      }
    }
  }
};
</script>

<style scoped lang="scss">
.simulate-container {
  max-width: 90%;
  width: 90%;
  margin: 5rem auto;
  padding: 2rem;
  text-align: center;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.06);
}

.qr-box {
  margin: 20rem 0 8rem;

  .qr-image {
    width: 180px;
    height: 180px;
  }

  p {
    margin-top: 1rem;
    font-size: 1.6rem;
    color: #555;
  }
}

.form-box {
  width: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1.5rem;
  margin: 25rem auto 8rem;

  input {
    width: 50%;
    padding: 1rem 1rem ;
    border-radius: 8px;
    border: 1px solid #ccc;
    font-size: 1.4rem;
  }
}

.confirm-btn {
  padding: 1rem 3rem;
  font-size: 1.6rem;
  background: #4a8eff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;

  &:hover {
    background: #3b76e0;
  }
}
</style>
