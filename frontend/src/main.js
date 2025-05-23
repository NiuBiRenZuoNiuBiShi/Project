import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router/router'
import '@/assets/global.scss'

import '@fortawesome/fontawesome-free/css/all.css'

import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

library.add(fas, fab);

const pinia = createPinia();

const app = createApp(App);
app.use(router);
pinia.use(piniaPluginPersistedstate);
app.use(pinia);
app.use(ElementPlus);
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount('#app');