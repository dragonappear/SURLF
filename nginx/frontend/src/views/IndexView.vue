<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import { ElMessageBox } from "element-plus";
import { useRouter } from "vue-router";

const shortLinks = ref([]);

const originalUrl = ref("");

const router = useRouter();

axios.get("/my-backend-api/").then((response) => {
    const dataArray = response.data.data;
    dataArray.forEach((r: any) => {
        shortLinks.value.push(r);
    });
});

const post = function () {
    axios
        .post("/my-backend-api/short-links", {
            url: originalUrl.value,
        })
        .then((response) => {
            const data = response.data.data;
            console.log(data);
            shortLinks.value.push(data);
            router.replace({ name: "index" });
            originalUrl.value = "";
        })
        .catch(error => {
            console.log(error.response.data);
            ElMessageBox.alert('Not valid url', 'Error', {
                confirmButtonText: 'OK'
            })
        });
};

</script>

<template>
    <header class="masthead">
        <div class="container position-relative">
            <div class="row justify-content-center">
                <div class="col-xl-8">
                    <div class="text-center text-white">
                        <h1 class="mb-5">Fast is Good, Faster is better.</h1>
                        <div class="row">
                            <div class="col">
                                <input class="form-control form-control-lg" v-model="originalUrl"
                                    placeholder="You can shorten your original link and track your short link.">
                            </div>

                            <div class="col-auto">
                                <button class="btn btn-primary btn-lg" id="submitButton" type="submit"
                                    @click="post()">Submit
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </header>

    <div class="container position-relative mt-5">
        <div class="justify-content-center">
            <table class="table table-hover" v-if="shortLinks.length > 0">
            </table>
            <el-table :data="shortLinks" stripe style="width: 100%">
                <el-table-column prop="createdAt" label="Date" width="250" />
                <el-table-column prop="shortId" label="shortId" width="180" />
                <el-table-column prop="url" label="url" />
                <el-table-column fixed="right" label="Detail" width="120">
                    <template #default>
                        <el-button link type="primary" size="small">Statistics</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>


<style>
header.masthead {
    position: relative;
    background: #343a40 url('@/assets/bg-masthead.jpg') no-repeat center center;
    background-size: cover;
    padding-top: 8rem;
    padding-bottom: 8rem;
}

header.masthead:before {
    content: "";
    position: absolute;
    background-color: #1c375e;
    height: 100%;
    width: 100%;
    top: 0;
    left: 0;
    opacity: 0.5;
}

header.masthead h1,
header.masthead .h1 {
    font-size: 2rem;
}

@media (min-width: 768px) {
    header.masthead {
        padding-top: 12rem;
        padding-bottom: 12rem;
    }

    header.masthead h1,
    header.masthead .h1 {
        font-size: 3rem;
    }
}
</style>
