<template>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container">
            <routerLink :class="route_name == 'home' ? 'nav-link active' : 'nav-link'" :to="{name: 'home'}">SnakeGPT</routerLink>
            <div class="collapse navbar-collapse" id="navbarText">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <routerLink :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'" aria-current="page" :to="{name: 'pk_index'}">对战</routerLink>
                    </li>
                    <li class="nav-item">
                        <routerLink :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'record_index'}">对局列表</routerLink>
                    </li>
                    <li class="nav-item">
                        <routerLink :class="route_name == 'rank_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'rank_index'}">排行榜</routerLink>
                    </li>
                </ul>
                
                <ul class="navbar-nav " v-if="$store.state.user.is_login">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown"
                            aria-expanded="false">
                            {{ $store.state.user.username }}
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                            <li><routerLink class="dropdown-item" :to="{name: 'user_bot_index'}">我的蛇</routerLink></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
                        </ul>
                    </li>
                </ul>

                <ul class="navbar-nav" v-else-if="!$store.state.user.pulling_info">
                    <li class="nav-item">
                        <router-link class="nav-link" :to="{name: 'user_account_login' }" role="button">
                            登录
                        </router-link>
                    </li>
                    <li class="nav-item">
                        <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
                            注册
                        </router-link>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script>
import { useRoute } from "vue-router";
import { computed } from "@vue/reactivity";
import { useStore } from 'vuex';

export default {
    setup() {
        const route = useRoute();
        const store = useStore();
        let route_name = computed(() => route.name);
        const logout = () => {
            store.dispatch("logout");
        }
        return {
            route_name,
            logout,
        }
    }
}

</script>

<style scoped>

</style>