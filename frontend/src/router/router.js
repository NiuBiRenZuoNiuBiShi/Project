import { createRouter, createWebHistory } from 'vue-router';
import Home from "@/views/Home.vue";
import Login from '@/views/Login.vue';
const routes = [
    {
        path: '/home',
        name: 'home',
        component: Home
    },
    {
        path: '/',
        name: 'login',
        component: Login
    }
];

const router = createRouter({
    routes,
    history: createWebHistory()
});

// router.beforeEach(async (to, form) => {
//     const token = localStorage.getItem('Authentication');
//     if (token) {
//         if (to.name === 'login') {
//             return { name: 'home' }
//         } else {
//             return true;
//         }
//     }
// })

export default router;