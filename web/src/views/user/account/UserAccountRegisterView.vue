<template>
        <div class="row justify-content-md-center margin">
            <div class="col-3">
                <form>
                    <div class="mb-3">
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <input v-model="password" type="password" class="form-control" id="password"
                            placeholder="请输入密码">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
</template>

<script>

</script>

<style scoped>
button {
    width: 100%;
}

div.error-message {
    color: red;
}

.margin {
    margin-top: 30px;
}
</style>

