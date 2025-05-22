import { createRouter, createWebHistory } from 'vue-router';
const routes = [
    {
        path: '',
        redirect: {name : 'Home'}
    },
    {
        path: '/',
        redirect: {name : 'Home'}
    },
    {
        path: '/home',
        name: 'Home',
        component: () => import('@/views/HomeView.vue')
    },
    {
        path: '/search',
        name: 'search',
        component: () => import('@/views/SearchView.vue')
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
    },
    {
        path: '/user',
        name: 'user',
        component: () => import('@/views/UserManagementView.vue')
    },
    {
        path: '/notifications',
        name: 'Notifications',
        component: () => import('@/views/MessageCenter.vue')
    },
    {
        path: '/message-detail/:id',
        name: 'MessageDetail',
        component: () => import('@/views/MessageDetail.vue'),
        props: true
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