<template>
  <div class="order-create">
    <div class="page-header">
      <h2>录入订单</h2>
      <p>创建新的洗衣订单</p>
    </div>
    
    <a-card :bordered="false">
      <a-steps :current="current" size="small">
        <a-step title="客户信息" />
        <a-step title="衣物信息" />
        <a-step title="订单确认" />
      </a-steps>
      
      <!-- 步骤1：客户信息 -->
      <div v-if="current === 0" class="steps-content">
        <a-form :form="form" layout="vertical">
          <a-row :gutter="16">
            <a-col :span="12">
              <a-form-item label="选择会员">
                <a-input-search
                  v-model="customerSearchText"
                  placeholder="输入会员姓名/手机号搜索"
                  enter-button
                  @search="searchCustomer"
                />
              </a-form-item>
              
              <div v-if="customerSearchResults.length > 0" class="customer-search-results">
                <a-list size="small" bordered>
                  <a-list-item v-for="customer in customerSearchResults" :key="customer.id" @click="selectCustomer(customer)">
                    <a-list-item-meta>
                      <template slot="title">
                        {{ customer.name }} ({{ customer.phone }})
                      </template>
                      <template slot="description">
                        会员ID: {{ customer.id }} | 余额: ¥{{ customer.balance }}
                      </template>
                    </a-list-item-meta>
                    <template slot="actions">
                      <a-button size="small" type="primary">选择</a-button>
                    </template>
                  </a-list-item>
                </a-list>
              </div>
              
              <div v-if="orderForm.customerId" class="selected-customer">
                <a-card size="small" title="已选择会员">
                  <a-descriptions :column="1">
                    <a-descriptions-item label="姓名">{{ orderForm.customerName }}</a-descriptions-item>
                    <a-descriptions-item label="手机号">{{ orderForm.customerPhone }}</a-descriptions-item>
                    <a-descriptions-item label="会员ID">{{ orderForm.customerId }}</a-descriptions-item>
                    <a-descriptions-item label="会员余额">¥{{ orderForm.customerBalance }}</a-descriptions-item>
                  </a-descriptions>
                </a-card>
              </div>
            </a-col>
            
            <a-col :span="12">
              <a-form-item label="创建新客户">
                <a-switch v-model="isNewCustomer" />
              </a-form-item>
              
              <div v-if="isNewCustomer">
                <a-form-item label="姓名">
                  <a-input v-model="newCustomer.name" placeholder="请输入客户姓名" />
                </a-form-item>
                <a-form-item label="手机号">
                  <a-input v-model="newCustomer.phone" placeholder="请输入手机号" />
                </a-form-item>
                <a-form-item label="是否创建会员">
                  <a-switch v-model="newCustomer.isMember" />
                </a-form-item>
                <div v-if="newCustomer.isMember">
                  <a-form-item label="初始充值">
                    <a-input-number v-model="newCustomer.initialBalance" :min="0" :step="10" placeholder="请输入初始充值金额" />
                  </a-form-item>
                </div>
              </div>
            </a-col>
          </a-row>
        </a-form>
      </div>
      
      <!-- 步骤2：衣物信息 -->
      <div v-if="current === 1" class="steps-content">
        <div class="step-content-header">
          <h3>添加衣物信息</h3>
          <a-button type="primary" icon="plus" @click="showAddClothesModal">添加衣物</a-button>
        </div>
        
        <a-table 
          :columns="clothesColumns" 
          :data-source="orderForm.clothes"
          :pagination="false"
          rowKey="id"
        >
          <template slot="operation" slot-scope="text, record, index">
            <a-button type="link" icon="edit" @click="editClothes(record, index)">编辑</a-button>
            <a-divider type="vertical" />
            <a-button type="link" icon="delete" @click="removeClothes(index)">删除</a-button>
          </template>
        </a-table>
        
        <div class="order-summary">
          <div class="order-total">
            <span>衣物总数: <a-tag color="blue">{{ orderForm.clothes.length }}</a-tag></span>
            <span>总金额: <a-tag color="green">¥{{ getTotalAmount() }}</a-tag></span>
          </div>
          <div class="order-remark">
            <a-form-item label="订单备注">
              <a-textarea v-model="orderForm.remark" :rows="2" placeholder="请输入订单备注" />
            </a-form-item>
          </div>
        </div>
      </div>
      
      <!-- 步骤3：订单确认 -->
      <div v-if="current === 2" class="steps-content">
        <a-card title="订单信息确认" :bordered="false">
          <a-descriptions title="客户信息" :column="2">
            <a-descriptions-item label="客户姓名">{{ orderForm.customerName }}</a-descriptions-item>
            <a-descriptions-item label="手机号码">{{ orderForm.customerPhone }}</a-descriptions-item>
            <a-descriptions-item label="会员ID" v-if="orderForm.customerId">{{ orderForm.customerId }}</a-descriptions-item>
            <a-descriptions-item label="会员余额" v-if="orderForm.customerId">¥{{ orderForm.customerBalance }}</a-descriptions-item>
          </a-descriptions>
          
          <a-divider />
          
          <a-descriptions title="衣物信息" :column="1">
            <a-descriptions-item label="衣物总数">{{ orderForm.clothes.length }}</a-descriptions-item>
            <a-descriptions-item label="总金额">¥{{ getTotalAmount() }}</a-descriptions-item>
          </a-descriptions>
          
          <a-table 
            :columns="clothesColumns" 
            :data-source="orderForm.clothes"
            :pagination="false"
            size="small"
            rowKey="id"
          >
            <template slot="operation" slot-scope="text, record, index">
              <a-button type="link" icon="edit" @click="editClothes(record, index)">编辑</a-button>
              <a-divider type="vertical" />
              <a-button type="link" icon="delete" @click="removeClothes(index)">删除</a-button>
            </template>
          </a-table>
          
          <a-divider />
          
          <a-descriptions title="支付信息" :column="1">
            <a-descriptions-item label="支付方式">
              <a-radio-group v-model="orderForm.paymentMethod">
                <a-radio value="CASH">现金</a-radio>
                <a-radio value="WECHAT">微信支付</a-radio>
                <a-radio value="ALIPAY">支付宝</a-radio>
                <a-radio value="MEMBER_CARD" :disabled="!orderForm.customerId || orderForm.customerBalance < getTotalAmount()">
                  会员卡 <a-tag v-if="orderForm.customerId" color="blue">余额: ¥{{ orderForm.customerBalance }}</a-tag>
                </a-radio>
              </a-radio-group>
            </a-descriptions-item>
            <a-descriptions-item label="支付状态">
              <a-switch
                v-model="orderForm.isPaid"
                checked-children="已支付"
                un-checked-children="未支付"
              />
            </a-descriptions-item>
            <a-descriptions-item label="订单备注" v-if="orderForm.remark">
              {{ orderForm.remark }}
            </a-descriptions-item>
          </a-descriptions>
        </a-card>
      </div>
      
      <div class="steps-action">
        <a-button v-if="current > 0" style="margin-right: 8px" @click="prev">上一步</a-button>
        <a-button
          v-if="current < 2"
          type="primary"
          @click="next"
          :disabled="(current === 0 && !validateStep1()) || (current === 1 && !validateStep2())"
        >
          下一步
        </a-button>
        <a-button v-if="current === 2" type="primary" @click="submitOrder">提交订单</a-button>
      </div>
    </a-card>
    
    <!-- 添加衣物弹窗 -->
    <a-modal
      v-model="clothesModalVisible"
      :title="clothesModalMode === 'add' ? '添加衣物' : '编辑衣物'"
      @ok="handleClothesModalOk"
      @cancel="handleClothesModalCancel"
      width="600px"
    >
      <a-form :form="clothesForm" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="衣物类别">
              <a-select
                v-model="clothesForm.categoryId"
                placeholder="请选择衣物类别"
                @change="handleCategoryChange"
              >
                <a-select-option v-for="cat in clothesCategories" :key="cat.id" :value="cat.id">
                  {{ cat.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="衣物名称">
              <a-input v-model="clothesForm.name" placeholder="请输入衣物名称" />
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="颜色">
              <a-select v-model="clothesForm.color" placeholder="请选择颜色">
                <a-select-option value="白色">白色</a-select-option>
                <a-select-option value="黑色">黑色</a-select-option>
                <a-select-option value="灰色">灰色</a-select-option>
                <a-select-option value="红色">红色</a-select-option>
                <a-select-option value="蓝色">蓝色</a-select-option>
                <a-select-option value="绿色">绿色</a-select-option>
                <a-select-option value="黄色">黄色</a-select-option>
                <a-select-option value="其他">其他</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="材质">
              <a-select v-model="clothesForm.material" placeholder="请选择材质">
                <a-select-option value="棉质">棉质</a-select-option>
                <a-select-option value="麻质">麻质</a-select-option>
                <a-select-option value="丝质">丝质</a-select-option>
                <a-select-option value="毛呢">毛呢</a-select-option>
                <a-select-option value="皮革">皮革</a-select-option>
                <a-select-option value="混纺">混纺</a-select-option>
                <a-select-option value="其他">其他</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="单价">
              <a-input-number
                v-model="clothesForm.price"
                :min="0"
                :step="1"
                placeholder="请输入单价"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="数量">
              <a-input-number
                v-model="clothesForm.quantity"
                :min="1"
                :step="1"
                placeholder="请输入数量"
                style="width: 100%"
              />
            </a-form-item>
          </a-col>
        </a-row>
        
        <a-form-item label="特殊处理要求">
          <a-checkbox-group v-model="clothesForm.specialRequirements">
            <a-checkbox value="急件">急件</a-checkbox>
            <a-checkbox value="轻柔洗涤">轻柔洗涤</a-checkbox>
            <a-checkbox value="特殊熨烫">特殊熨烫</a-checkbox>
            <a-checkbox value="高温消毒">高温消毒</a-checkbox>
            <a-checkbox value="去渍处理">去渍处理</a-checkbox>
          </a-checkbox-group>
        </a-form-item>
        
        <a-form-item label="备注">
          <a-textarea v-model="clothesForm.remark" :rows="2" placeholder="请输入备注" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
export default {
  name: 'OrderCreate',
  data() {
    return {
      form: this.$form.createForm(this),
      current: 0,
      orderForm: {
        customerId: null,
        customerName: '',
        customerPhone: '',
        customerBalance: 0,
        clothes: [],
        remark: '',
        paymentMethod: 'CASH',
        isPaid: false
      },
      customerSearchText: '',
      customerSearchResults: [],
      isNewCustomer: false,
      newCustomer: {
        name: '',
        phone: '',
        isMember: false,
        initialBalance: 0
      },
      clothesModalVisible: false,
      clothesModalMode: 'add', // 'add' or 'edit'
      editingClothesIndex: -1,
      clothesForm: {
        id: null,
        categoryId: null,
        categoryName: '',
        name: '',
        color: '',
        material: '',
        price: 0,
        quantity: 1,
        specialRequirements: [],
        remark: ''
      },
      clothesCategories: [
        { id: 1, name: '上衣' },
        { id: 2, name: '裤子' },
        { id: 3, name: '裙子' },
        { id: 4, name: '外套' },
        { id: 5, name: '羽绒服' },
        { id: 6, name: '床单' },
        { id: 7, name: '被罩' },
        { id: 8, name: '窗帘' },
        { id: 9, name: '鞋子' },
        { id: 10, name: '包包' },
        { id: 11, name: '其他' }
      ],
      clothesColumns: [
        {
          title: '类别',
          dataIndex: 'categoryName',
          key: 'categoryName'
        },
        {
          title: '名称',
          dataIndex: 'name',
          key: 'name'
        },
        {
          title: '颜色',
          dataIndex: 'color',
          key: 'color'
        },
        {
          title: '单价',
          dataIndex: 'price',
          key: 'price',
          customRender: (text) => `¥${text}`
        },
        {
          title: '数量',
          dataIndex: 'quantity',
          key: 'quantity'
        },
        {
          title: '金额',
          key: 'amount',
          customRender: (text, record) => `¥${(record.price * record.quantity).toFixed(2)}`
        },
        {
          title: '操作',
          key: 'operation',
          scopedSlots: { customRender: 'operation' }
        }
      ]
    }
  },
  methods: {
    // 步骤导航
    next() {
      if (this.current === 0 && !this.validateStep1()) {
        this.$message.error('请先选择或创建客户');
        return;
      }
      if (this.current === 1 && !this.validateStep2()) {
        this.$message.error('请至少添加一件衣物');
        return;
      }
      this.current++;
    },
    prev() {
      this.current--;
    },
    
    // 步骤验证
    validateStep1() {
      if (this.isNewCustomer) {
        return this.newCustomer.name && this.newCustomer.phone;
      } else {
        return this.orderForm.customerId && this.orderForm.customerName && this.orderForm.customerPhone;
      }
    },
    validateStep2() {
      return this.orderForm.clothes && this.orderForm.clothes.length > 0;
    },
    
    // 客户相关
    searchCustomer() {
      if (!this.customerSearchText) {
        this.$message.warning('请输入搜索内容');
        return;
      }
      
      // 模拟API请求
      this.$message.loading('搜索中...', 1).then(() => {
        // 这里应该调用后端API进行搜索
        const mockResults = [
          { id: 1, name: '张三', phone: '13800138000', balance: 200 },
          { id: 2, name: '李四', phone: '13900139000', balance: 350 },
          { id: 3, name: '王五', phone: '13700137000', balance: 100 }
        ];
        
        this.customerSearchResults = mockResults.filter(item => 
          item.name.includes(this.customerSearchText) || 
          item.phone.includes(this.customerSearchText)
        );
        
        if (this.customerSearchResults.length === 0) {
          this.$message.info('未找到符合条件的会员');
        }
      });
    },
    selectCustomer(customer) {
      this.orderForm.customerId = customer.id;
      this.orderForm.customerName = customer.name;
      this.orderForm.customerPhone = customer.phone;
      this.orderForm.customerBalance = customer.balance;
      this.isNewCustomer = false;
      this.customerSearchResults = [];
      this.customerSearchText = '';
    },
    
    // 衣物相关
    showAddClothesModal() {
      this.clothesModalMode = 'add';
      this.clothesForm = {
        id: Date.now(),
        categoryId: null,
        categoryName: '',
        name: '',
        color: '',
        material: '',
        price: 0,
        quantity: 1,
        specialRequirements: [],
        remark: ''
      };
      this.clothesModalVisible = true;
    },
    editClothes(record, index) {
      this.clothesModalMode = 'edit';
      this.editingClothesIndex = index;
      this.clothesForm = { ...record };
      this.clothesModalVisible = true;
    },
    removeClothes(index) {
      this.orderForm.clothes.splice(index, 1);
    },
    handleClothesModalOk() {
      // 验证表单
      if (!this.clothesForm.categoryId) {
        this.$message.error('请选择衣物类别');
        return;
      }
      if (!this.clothesForm.name) {
        this.$message.error('请输入衣物名称');
        return;
      }
      if (this.clothesForm.price <= 0) {
        this.$message.error('请输入有效的价格');
        return;
      }
      if (this.clothesForm.quantity <= 0) {
        this.$message.error('请输入有效的数量');
        return;
      }
      
      const clothes = { ...this.clothesForm };
      
      // 根据类别ID获取类别名称
      const category = this.clothesCategories.find(item => item.id === clothes.categoryId);
      if (category) {
        clothes.categoryName = category.name;
      }
      
      if (this.clothesModalMode === 'add') {
        this.orderForm.clothes.push(clothes);
      } else if (this.clothesModalMode === 'edit') {
        this.orderForm.clothes.splice(this.editingClothesIndex, 1, clothes);
      }
      
      this.clothesModalVisible = false;
    },
    handleClothesModalCancel() {
      this.clothesModalVisible = false;
    },
    handleCategoryChange(value) {
      const category = this.clothesCategories.find(item => item.id === value);
      if (category) {
        this.clothesForm.categoryName = category.name;
      }
    },
    
    // 计算总金额
    getTotalAmount() {
      return this.orderForm.clothes.reduce((sum, item) => sum + (item.price * item.quantity), 0).toFixed(2);
    },
    
    // 提交订单
    submitOrder() {
      // 处理新客户的逻辑
      if (this.isNewCustomer) {
        // 这里应该先调用创建客户的API
        this.orderForm.customerName = this.newCustomer.name;
        this.orderForm.customerPhone = this.newCustomer.phone;
        
        if (this.newCustomer.isMember) {
          // 创建会员的逻辑
          this.orderForm.customerBalance = this.newCustomer.initialBalance;
        }
      }
      
      // 构建提交的订单数据
      const orderData = {
        customerId: this.orderForm.customerId,
        customerName: this.orderForm.customerName,
        customerPhone: this.orderForm.customerPhone,
        clothes: this.orderForm.clothes,
        remark: this.orderForm.remark,
        paymentMethod: this.orderForm.paymentMethod,
        isPaid: this.orderForm.isPaid,
        totalAmount: this.getTotalAmount()
      };
      
      // 提交订单
      console.log('Submit order data:', orderData);
      
      // 模拟API请求
      this.$message.loading('提交中...', 2).then(() => {
        this.$message.success('订单创建成功');
        // 提交成功后，跳转到订单列表或详情页
        this.$router.push('/order/list');
      });
    }
  }
}
</script>

<style lang="less" scoped>
.order-create {
  .page-header {
    margin-bottom: 24px;
    
    h2 {
      margin-bottom: 8px;
    }
    
    p {
      color: rgba(0, 0, 0, 0.45);
    }
  }
  
  .steps-content {
    margin-top: 24px;
    padding: 24px;
    background-color: #fafafa;
    min-height: 200px;
  }
  
  .steps-action {
    margin-top: 24px;
  }
  
  .customer-search-results {
    margin-bottom: 16px;
    max-height: 300px;
    overflow-y: auto;
  }
  
  .selected-customer {
    margin-top: 16px;
  }
  
  .step-content-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
  }
  
  .order-summary {
    margin-top: 16px;
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
  }
  
  .order-total {
    display: flex;
    justify-content: flex-end;
    gap: 16px;
    margin-bottom: 16px;
  }
}
</style> 