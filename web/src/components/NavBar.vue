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
                
                <ul class="navbar-nav ">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown"
                            aria-expanded="false">
                            沃斯尼蝶
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                            <li><routerLink class="dropdown-item" :to="{name: 'user_bot_index'}">我的蛇</routerLink></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item" href="#">退出</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</template>

<script>
import { useRoute } from "vue-router";
import { computed } from "@vue/reactivity";

export default {
    setup() {
        const route = useRoute();
        let route_name = computed(() => route.name);
        return {
            route_name
        }
    }
}

</script>

<style scoped>

</style>