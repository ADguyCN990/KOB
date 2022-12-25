<template>

    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-body">
                    <img class="img-fluid" :src="$store.state.user.photo" alt="" style="width: 19.2vw; height: 19.2vw;">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top: 20px;">
                    <div class="card-header">
                        <span style="font-size: 130%">我的Bot</span>
                        <!-- Button trigger modal -->
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-button">
                            创建Bot
                        </button>
                        
                    
                        <!-- Modal -->
                        <div class="modal fade" id="add-bot-button" tabindex="-1" >
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h1 class="modal-title fs-5" id="exampleModalLabel">创建Bot</h1>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <form>
                                            <div class="mb-3">
                                                <label for="add-bot-title" class="form-label">名称</label>
                                                <input type="text" v-model="botadd.title" class="form-control" id="add-bot-title" placeholder="请输入Bot名称">
                                                
                                            </div>
                                            <div class="mb-3">
                                                <label for="add-bot-description" class="form-label">简介</label>
                                                <textarea class="form-control" v-model="botadd.description" id="add-bot-description" placeholder="请输入Bot简介"></textarea>
                                            </div>
                                            <div class="mb-3">
                                                <label for="add-bot-code" class="form-label">代码</label>
                                                
                                                <VAceEditor v-model:value="botadd.content" v-model="botadd.content" @init="editorInit" lang="c_cpp" theme="textmate" style="height: 300px" ></VAceEditor>   
                                            </div>

                                        </form>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="error-message" style="color: red">  {{ botadd.error_message }} </div>
                                        <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td> 
                                    <td> 
                                        <button type="button" class="btn btn-secondary" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal' + bot.id">修改</button>
                                        <button type="button" class="btn btn-danger" style="margin-left: 10px" @click="remove_bot(bot)">删除</button>

                                    </td>
                                    <!-- Modal -->
                                    <div class="modal fade" :id="'update-bot-modal' + bot.id" tabindex="-1">
                                        <div class="modal-dialog modal-xl">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5" id="exampleModalLabel">修改Bot</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <form>
                                                        <div class="mb-3">
                                                            <label for="add-bot-title" class="form-label">名称</label>
                                                            <input type="text" v-model="bot.title" class="form-control" id="add-bot-title"
                                                                placeholder="请输入Bot名称">
                                    
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-description" class="form-label">简介</label>
                                                            <textarea class="form-control" v-model="bot.description" id="add-bot-description"
                                                                placeholder="请输入Bot简介"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-code" class="form-label">代码</label>
                                                            <VAceEditor v-model:value="bot.content" v-model="botadd.content" @init="editorInit" lang="c_cpp" theme="textmate"
                                                                style="height: 300px"></VAceEditor>
                                                        </div>
                                    
                                                    </form>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class="error-message" style="color: red"> {{ bot.error_message }} </div>
                                                    <button type="button" class="btn btn-primary" @click="update_bot(bot)">修改</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </tr>
                                
                            </tbody>
                        </table>
                    </div>
                    


                </div>
            </div>
        </div>
    </div>

</template>


<script>
import $ from 'jquery'
import { ref, reactive } from 'vue'
import { Modal } from 'bootstrap/dist/js/bootstrap'
//import { useStore } from 'vuex'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default {
    components: {
        VAceEditor,
    },
    setup() {
        //const store = useStore();
        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
        const jwt_token = localStorage.getItem("jwt_token");



        let bots = ref([]);
        let botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: "",
        });

        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + jwt_token,
                },
                success(resp) {
                    bots.value = resp;
                },

            })
        }
        refresh_bots();
        const add_bot = () => {
            botadd.error_message = "";
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/add/",
                type: "post",
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                },
                headers: {
                    Authorization: "Bearer " + jwt_token,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        Modal.getInstance("#add-bot-button").hide();
                        refresh_bots();
                    }
                    else {
                        botadd.error_message = resp.error_message;
                    }
                },

            })
        }
        const remove_bot = (bot) => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + jwt_token,
                },
                success() {
                    refresh_bots();
                }
            })
        }
        const update_bot = (bot) => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    Authorization: "Bearer " + jwt_token,
                },
                success(resp) {
                    if (resp.error_message === "update success") {
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        Modal.getInstance('#update-bot-modal' + bot.id).hide();
                        refresh_bots();
                    }
                    else {
                        botadd.error_message = resp.error_message;
                    }
                },

            })
        }
        return {
            bots,
            botadd,
            add_bot,
            remove_bot,
            update_bot,
        }

        
    }
}

</script>

<style scoped>

</style>