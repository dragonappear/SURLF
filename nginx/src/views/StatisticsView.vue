<script setup lang="ts">
import axios from "axios";
import { onMounted, ref } from "vue";


interface DataItem {
    shortId: string;
    url: string;
    createdAt: string;
}

interface Response {
    date: string;
    count: string;
}

interface StatisticsItem {
    shortId: string;
    date: string;
    count: string;
}

const shortLinks = ref<DataItem[]>([]);
const statistics = ref<StatisticsItem[]>([]);

onMounted(() => {
    axios.get("/my-backend-api/").then((response) => {
        const dataArray = response.data.data;
        dataArray.forEach((r: DataItem) => {
            shortLinks.value.push(r);
        });
    });
});

const getStatistics = function (shortId: string) {
    axios.get(`/my-backend-api/statistics/${shortId}`).then((response) => {
        statistics.value = [];
        const dataArray = response.data.data;
        dataArray.forEach((r: Response) => {
            const item: StatisticsItem = {
                shortId: shortId,
                date: r.date,
                count: r.count
            }
            statistics.value.push(item);
        });
    });
};

</script>

<template>
    <div class="row">
        <div class="col-md-4">
            <div class="container position-relative mt-5">
                <div class="justify-content-center">
                    <table class="table table-hover" v-if="shortLinks.length > 0"></table>
                    <el-table :data="shortLinks" stripe style="width: 100%">
                        <el-table-column prop="shortId" label="shortId" width="100" />
                        <el-table-column prop="url" label="url" />
                        <el-table-column fixed="right" label="Detail" width="120">
                            <template #default="{ row }">
                                <el-button type="primary" size="small"
                                    @click="getStatistics.bind(null, row.shortId)()">Statistics</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="container position-relative mt-5">
                <h1 class="text-center">Statistics : Short link counts</h1>
                <div class="justify-content-center mt-5">
                    <table class="table table-hover" v-if="shortLinks.length > 0"></table>
                    <el-table :data="statistics" stripe style="width: 100%">
                        <el-table-column prop="shortId" label="ShortId" />
                        <el-table-column prop="date" label="Date" />
                        <el-table-column prop="count" label="Count" />
                    </el-table>
                </div>
            </div>
        </div>
    </div>
</template>

<style></style>