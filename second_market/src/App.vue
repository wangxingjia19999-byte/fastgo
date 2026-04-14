<script setup>
import { computed, reactive, ref } from 'vue'

const categories = ref([
  { id: 1, name: '教材书籍' },
  { id: 2, name: '数码配件' },
  { id: 3, name: '生活用品' }
])

const products = ref([
  {
    id: 1,
    title: '高等数学（同济第七版）',
    price: 25,
    categoryId: 1,
    condition: '8成新',
    owner: '李同学',
    contact: '微信: li_study01',
    description: '有少量笔记，期末复习够用。',
    createdAt: '2026-04-10'
  },
  {
    id: 2,
    title: '机械键盘 87 键（青轴）',
    price: 88,
    categoryId: 2,
    condition: '9成新',
    owner: '王同学',
    contact: 'QQ: 30214567',
    description: '灯效正常，送一根备用数据线。',
    createdAt: '2026-04-11'
  }
])

const records = ref([
  {
    id: 1,
    type: '留言',
    content: '求一本离散数学教材，价格 30 以内。',
    author: '赵同学',
    time: '2026-04-12 19:30'
  }
])

const activeCategory = ref('all')
const categoryInput = ref('')
const editingCategoryId = ref(null)
const editingCategoryName = ref('')

const productForm = reactive({
  title: '',
  price: '',
  categoryId: 1,
  condition: '',
  owner: '',
  contact: '',
  description: ''
})

const editingProductId = ref(null)

const recordForm = reactive({
  type: '留言',
  content: '',
  author: ''
})

const editingRecordId = ref(null)

const currentModeText = computed(() =>
  editingProductId.value ? '编辑商品' : '发布商品'
)

const productCount = computed(() => products.value.length)
const categoryCount = computed(() => categories.value.length)

const filteredProducts = computed(() => {
  if (activeCategory.value === 'all') return products.value
  return products.value.filter((p) => p.categoryId === Number(activeCategory.value))
})

const getCategoryName = (categoryId) => {
  return categories.value.find((c) => c.id === categoryId)?.name || '未分类'
}

const resetProductForm = () => {
  productForm.title = ''
  productForm.price = ''
  productForm.categoryId = categories.value[0]?.id || 1
  productForm.condition = ''
  productForm.owner = ''
  productForm.contact = ''
  productForm.description = ''
  editingProductId.value = null
}

const submitProduct = () => {
  if (!productForm.title.trim() || !productForm.owner.trim()) return

  const payload = {
    title: productForm.title.trim(),
    price: Number(productForm.price) || 0,
    categoryId: Number(productForm.categoryId),
    condition: productForm.condition.trim() || '自定义',
    owner: productForm.owner.trim(),
    contact: productForm.contact.trim() || '请站内留言联系',
    description: productForm.description.trim() || '暂无描述',
    createdAt: new Date().toISOString().slice(0, 10)
  }

  if (editingProductId.value) {
    products.value = products.value.map((item) =>
      item.id === editingProductId.value ? { ...item, ...payload } : item
    )
  } else {
    products.value.unshift({
      id: Date.now(),
      ...payload
    })
  }

  resetProductForm()
}

const editProduct = (product) => {
  editingProductId.value = product.id
  productForm.title = product.title
  productForm.price = product.price
  productForm.categoryId = product.categoryId
  productForm.condition = product.condition
  productForm.owner = product.owner
  productForm.contact = product.contact
  productForm.description = product.description
}

const deleteProduct = (id) => {
  products.value = products.value.filter((item) => item.id !== id)
  if (editingProductId.value === id) {
    resetProductForm()
  }
}

const addCategory = () => {
  const name = categoryInput.value.trim()
  if (!name) return
  categories.value.push({
    id: Date.now(),
    name
  })
  categoryInput.value = ''
}

const startEditCategory = (category) => {
  editingCategoryId.value = category.id
  editingCategoryName.value = category.name
}

const saveCategory = (id) => {
  const name = editingCategoryName.value.trim()
  if (!name) return
  categories.value = categories.value.map((item) =>
    item.id === id ? { ...item, name } : item
  )
  editingCategoryId.value = null
  editingCategoryName.value = ''
}

const deleteCategory = (id) => {
  const used = products.value.some((item) => item.categoryId === id)
  if (used) return
  categories.value = categories.value.filter((item) => item.id !== id)
  if (Number(activeCategory.value) === id) {
    activeCategory.value = 'all'
  }
}

const submitRecord = () => {
  if (!recordForm.content.trim() || !recordForm.author.trim()) return

  const payload = {
    type: recordForm.type,
    content: recordForm.content.trim(),
    author: recordForm.author.trim(),
    time: new Date().toLocaleString('zh-CN', { hour12: false })
  }

  if (editingRecordId.value) {
    records.value = records.value.map((item) =>
      item.id === editingRecordId.value ? { ...item, ...payload } : item
    )
  } else {
    records.value.unshift({
      id: Date.now(),
      ...payload
    })
  }

  resetRecordForm()
}

const editRecord = (record) => {
  editingRecordId.value = record.id
  recordForm.type = record.type
  recordForm.content = record.content
  recordForm.author = record.author
}

const resetRecordForm = () => {
  editingRecordId.value = null
  recordForm.type = '留言'
  recordForm.content = ''
  recordForm.author = ''
}

const deleteRecord = (id) => {
  records.value = records.value.filter((item) => item.id !== id)
  if (editingRecordId.value === id) {
    resetRecordForm()
  }
}
</script>

<template>
  <div class="page">
    <header class="hero">
      <h1>校内二手物品交易平台</h1>
      <div class="stats">
        <span>商品 {{ productCount }}</span>
        <span>分类 {{ categoryCount }}</span>
        <span>记录 {{ records.length }}</span>
      </div>
    </header>

    <main class="layout">
      <aside class="panel left-panel">
        <section>
          <div class="section-title">分类管理</div>
          <div class="input-row">
            <input v-model="categoryInput" placeholder="新增分类名称" />
            <button class="btn solid" @click="addCategory">新增</button>
          </div>

          <div class="category-filter">
            <button
              class="chip"
              :class="{ active: activeCategory === 'all' }"
              @click="activeCategory = 'all'"
            >
              全部
            </button>
            <button
              v-for="cat in categories"
              :key="cat.id"
              class="chip"
              :class="{ active: Number(activeCategory) === cat.id }"
              @click="activeCategory = String(cat.id)"
            >
              {{ cat.name }}
            </button>
          </div>

          <ul class="category-list">
            <li v-for="cat in categories" :key="cat.id">
              <template v-if="editingCategoryId === cat.id">
                <input v-model="editingCategoryName" />
                <button class="btn" @click="saveCategory(cat.id)">保存</button>
              </template>
              <template v-else>
                <span>{{ cat.name }}</span>
                <div class="actions">
                  <button class="btn" @click="startEditCategory(cat)">改名</button>
                  <button class="btn danger" @click="deleteCategory(cat.id)">删除</button>
                </div>
              </template>
            </li>
          </ul>
        </section>

        <section>
          <div class="section-title">留言 / 求购记录</div>
          <div class="input-col">
            <select v-model="recordForm.type">
              <option>留言</option>
              <option>求购</option>
            </select>
            <input v-model="recordForm.author" placeholder="你的昵称" />
            <textarea v-model="recordForm.content" rows="3" placeholder="输入文本内容"></textarea>
            <div class="form-actions">
              <button class="btn solid" @click="submitRecord">{{ editingRecordId ? '保存记录' : '发布记录' }}</button>
              <button v-if="editingRecordId" class="btn" @click="resetRecordForm">取消</button>
            </div>
          </div>

          <ul class="record-list">
            <li v-for="item in records" :key="item.id">
              <div class="record-head">
                <p class="record-meta">{{ item.type }} · {{ item.author }} · {{ item.time }}</p>
                <div class="actions">
                  <button class="btn" @click="editRecord(item)">编辑</button>
                  <button class="btn danger" @click="deleteRecord(item.id)">删除</button>
                </div>
              </div>
              <p>{{ item.content }}</p>
            </li>
          </ul>
        </section>
      </aside>

      <section class="panel right-panel">
        <div class="section-title">{{ currentModeText }}</div>
        <div class="form-grid">
          <input v-model="productForm.title" placeholder="商品名称" />
          <input v-model="productForm.owner" placeholder="发布人" />
          <input v-model="productForm.contact" placeholder="联系方式（微信/QQ/手机号）" />
          <input v-model="productForm.price" type="number" min="0" placeholder="价格（元）" />
          <input v-model="productForm.condition" placeholder="成色（如：9成新）" />
          <select v-model="productForm.categoryId">
            <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
          </select>
          <textarea v-model="productForm.description" rows="3" placeholder="商品描述"></textarea>
        </div>

        <div class="form-actions">
          <button class="btn solid" @click="submitProduct">{{ editingProductId ? '保存修改' : '发布商品' }}</button>
          <button class="btn" @click="resetProductForm">重置</button>
        </div>

        <div class="section-title">商品列表</div>
        <div class="product-grid">
          <article v-for="item in filteredProducts" :key="item.id" class="card">
            <p class="card-tag">{{ getCategoryName(item.categoryId) }}</p>
            <h3>{{ item.title }}</h3>
            <p class="price">¥ {{ item.price }}</p>
            <p class="desc">{{ item.description }}</p>
            <p class="meta">{{ item.owner }} · {{ item.contact }} · {{ item.condition }} · {{ item.createdAt }}</p>
            <div class="actions">
              <button class="btn" @click="editProduct(item)">编辑</button>
              <button class="btn danger" @click="deleteProduct(item.id)">删除</button>
            </div>
          </article>

          <div v-if="filteredProducts.length === 0" class="empty">
            当前分类暂无商品，试试新增一条。
          </div>
        </div>
      </section>
    </main>
  </div>
</template>
