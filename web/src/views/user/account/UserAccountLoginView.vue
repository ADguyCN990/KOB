<template>
        <div class="row justify-content-md-center margin">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <input v-model="password" type="password" class="form-control" id="password"
                            placeholder="请输入密码">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">登录</button>
                </form>
            </div>
        </div>
</template>

<script>
import { useStore } from 'vuex'
import { ref } from 'vue'
import router from '../../../router/index'

export default {
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');
        //触发函数
        const login = () => {
            error_message.value = "";
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    //成功后跳转到主页面
                    store.dispatch("getinfo", {
                        success() {
                            router.push({ name: 'pk_index' });
                        }
                    })
                },
                error() {
                    error_message.value = "用户名或密码错误";
                }
            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>

<style scoped>
button {
    width: 100%;
}

div.error-message {
    color: red;
}

.margin {
    margin-top: 19vh;
}

.error-message {
    margin-bottom: 20px;
}
</style>

