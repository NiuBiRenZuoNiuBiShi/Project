import { createRouter, createWebHistory } from 'vue-router';
const routes = [
    {
        path: '/home',
        name: 'home',
        component: () => import('@/views/Home.vue')
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/trains/:pathMatch(.*)*',
        name: 'trains',
        component: () => import('@/views/TrainListView.vue')
    },
    {
        path: '/trainfood/:id',
        name: 'orderFood',
        component: () => import('@/views/FoodOrderView.vue')
    },
    {
        path: '/test',
        name: 'test',
        component: () => import('@/views/TestView.vue')
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