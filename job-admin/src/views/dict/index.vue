<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select
        v-model="listQuery.type"
        placeholder="字典类型"
        clearable
        filterable
        style="width: 200px"
        class="filter-item"
        @change="handleFilter"
      >
        <el-option
          v-for="(item, index) in dictTypeOptions"
          :key="index"
          :label="item.name"
          :value="item.value"
        />
      </el-select>
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >搜索</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="success"
        icon="el-icon-edit"
        @click="handleCreate"
      >添加字段</el-button>
    </div>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top:10px"
      @sort-change="sortChange"
    >
      <el-table-column
        label="ID"
        prop="id"
        sortable="custom"
        align="center"
        width="80"
        :class-name="getSortClass('id')"
      >
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="字段类型">
        <template slot-scope="{ row }">
          <span>{{ getDictType(row.type) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="字段名" prop="name" align="center"></el-table-column>
      <el-table-column label="字段值">
        <template slot-scope="scope">
          <span>{{ scope.row.value }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,'deleted')">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="handleRouteList"
    />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :model="dictForm"
        :rules="rules"
        label-position="left"
        label-width="100px"
        style="width: 300px; margin-left:50px;"
      >
        <el-form-item label="字段类型" prop="type">
          <el-select
            v-model="dictForm.type"
            class="filter-item"
            placeholder="请选择字段类型"
            style="width: 300px"
            filterable
          >
            <el-option
              v-for="(item, index) in dictTypeOptions"
              :key="index"
              :label="item.name"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="字段名" prop="name">
          <el-input v-model="dictForm.name" placeholder="请填写字段名" style="width: 300px"></el-input>
        </el-form-item>
        <el-form-item label="字段值" prop="value">
          <el-input v-model="dictForm.value" placeholder="请填写字段值" style="width: 300px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="saveData()">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDictList, saveDict, deleteDict } from "@/api/dict_api";
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination

export default {
  name: "DictList",
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        type: undefined,
        sort: "-id"
      },
      dictTypeOptions: [
        {
          name: "学历",
          value: 1
        },
        {
          name: "城市",
          value: 2
        },
        {
          name: "技能",
          value: 3
        },
        {
          name: "公司阶段",
          value: 4
        },
        {
          name: "公司规模",
          value: 5
        },
        {
          name: "行业",
          value: 6
        },
        {
          name: "企业性质",
          value: 7
        },
        {
          name: "快递类型",
          value: 8
        },
        {
          name: "职位类型",
          value: 9
        },
        {
          name: "薪资范围",
          value: 9
        },
        {
          name: "GPA范围",
          value: 10
        }
      ],
      dictForm: {
        id: undefined,
        name: "",
        type: undefined,
        value: ""
      },
      dialogFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "修改字段",
        create: "增加字段"
      },
      rules: {
        type: [
          { required: true, message: "字段类型不能为空", trigger: "change" }
        ],
        name: [
          { required: true, message: "字段名称不能为空", trigger: "change" }
        ],
        value: [
          { required: true, message: "字段值不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  watch:{
   $route(){
     this.getList();
   }
  },
  methods: {
    getList() {
      this.listLoading = true;
      const query = this.$route.query;
      Object.keys(query).forEach(key => {
        this.listQuery[key] = query[key];
      });
      if (query.type) {
        this.listQuery.page = parseInt(query.type);
      }
      if (query.page) {
        this.listQuery.page = parseInt(query.page);
      }
      if (query.limit) {
        this.listQuery.limit = parseInt(query.limit);
      }
      getDictList(this.listQuery).then(response => {
        this.list = response.data.list;
        this.total = response.data.total;
        this.listLoading = false;
      });
    },
    handleRouteList() {
      this.$router.push({ path: this.$route.path, query: this.listQuery });
    },
    handleFilter() {
      this.listQuery.page = 1;
      this.handleRouteList();
    },
    sortChange(data) {
      const { prop, order } = data;
      if (order === "ascending") {
        this.listQuery.sort = "+" + prop;
      } else {
        this.listQuery.sort = "-" + prop;
      }
      this.handleFilter();
    },
    resetForm() {
      this.dictForm = {
        patternId: undefined,
        colorId: undefined,
        increment: undefined,
        qualities: []
      };
    },
    handleCreate() {
      this.resetForm();
      this.dialogStatus = "create";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    saveData() {
      this.$refs["dataForm"].validate(valid => {
        if (valid) {
          saveDict(this.dictForm).then(response => {
            this.getList();
            this.dialogFormVisible = false;
            this.$notify({
              title: "成功提示",
              message: "保存成功",
              type: "success",
              duration: 2000
            });
          });
        }
      });
    },
    handleUpdate(row) {
      this.dictForm = Object.assign({}, row);
      this.dialogStatus = "update";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    handleDelete(row) {
      this.$confirm("此操作将删除一条记录, 是否继续?", "提示", {
        confirmButtonText: "继续删除",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          console.log("comfirm delete : " + row.id);
          deleteDict(row.id).then(() => {
            const index = this.list.indexOf(row);
            this.list.splice(index, 1);
            this.$message({
              type: "success",
              message: "删除成功!"
            });
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort;
      return sort === `+${key}`
        ? "ascending"
        : sort === `-${key}`
        ? "descending"
        : "";
    },
    getDictType(type) {
      const dictType = this.dictTypeOptions.find(d => d.value === type);
      return dictType? dictType.name: '';
    }
  }
};
</script>
