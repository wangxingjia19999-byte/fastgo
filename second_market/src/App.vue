<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'

const categories = ref([])
const products = ref([])
const records = ref([])

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

const getCategoryName = (categoryId) => {
  return categories.value.find((c) => c.id === categoryId)?.name || '未分类'
}

const request = async (url, options = {}) => {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  })
  if (!response.ok) {
    throw new Error(`请求失败: ${response.status}`)
  }
  if (response.status === 204) return null
  const text = await response.text()
  return text ? JSON.parse(text) : null
}

const loadCategories = async () => {
  const data = await request('/api/categories')
  categories.value = Array.isArray(data) ? data : []
  if (activeCategory.value !== 'all') {
    const current = Number(activeCategory.value)
    if (!categories.value.some((item) => item.id === current)) {
      activeCategory.value = 'all'
    }
  }
  if (!categories.value.some((item) => item.id === Number(productForm.categoryId))) {
    productForm.categoryId = categories.value[0]?.id || 1
  }
}

const loadProducts = async () => {
  const url = activeCategory.value === 'all'
    ? '/api/goods'
    : `/api/goods?categoryId=${encodeURIComponent(activeCategory.value)}`
  const data = await request(url)
  products.value = Array.isArray(data) ? data : []
}

const loadRecords = async () => {
  const data = await request('/api/messages')
  records.value = Array.isArray(data) ? data : []
}

const loadAll = async () => {
  try {
    await Promise.all([loadCategories(), loadProducts(), loadRecords()])
  } catch (error) {
    console.error(error)
    alert('后端连接失败，请确认后端已启动并且数据库可访问。')
  }
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
    id: editingProductId.value,
    title: productForm.title.trim(),
    price: Number(productForm.price) || 0,
    categoryId: Number(productForm.categoryId),
    condition: productForm.condition.trim() || '自定义',
    owner: productForm.owner.trim(),
    contact: productForm.contact.trim() || '请站内留言联系',
    description: productForm.description.trim() || '暂无描述',
    createdAt: new Date().toISOString().slice(0, 10)
  }

  const run = async () => {
    try {
      if (editingProductId.value) {
        await request(`/api/goods/${editingProductId.value}`, {
          method: 'PUT',
          body: JSON.stringify(payload)
        })
      } else {
        await request('/api/goods', {
          method: 'POST',
          body: JSON.stringify(payload)
        })
      }
      await loadProducts()
      resetProductForm()
    } catch (error) {
      console.error(error)
      alert('商品保存失败')
    }
  }
  run()
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
  const run = async () => {
    try {
      await request(`/api/goods/${id}`, { method: 'DELETE' })
      await loadProducts()
      if (editingProductId.value === id) {
        resetProductForm()
      }
    } catch (error) {
      console.error(error)
      alert('商品删除失败')
    }
  }
  run()
}

const addCategory = () => {
  const name = categoryInput.value.trim()
  if (!name) return
  const run = async () => {
    try {
      await request('/api/categories', {
        method: 'POST',
        body: JSON.stringify({ name })
      })
      categoryInput.value = ''
      await loadCategories()
    } catch (error) {
      console.error(error)
      alert('分类新增失败')
    }
  }
  run()
}

const startEditCategory = (category) => {
  editingCategoryId.value = category.id
  editingCategoryName.value = category.name
}

const saveCategory = (id) => {
  const name = editingCategoryName.value.trim()
  if (!name) return
  const run = async () => {
    try {
      await request(`/api/categories/${id}`, {
        method: 'PUT',
        body: JSON.stringify({ id, name })
      })
      editingCategoryId.value = null
      editingCategoryName.value = ''
      await loadCategories()
    } catch (error) {
      console.error(error)
      alert('分类更新失败')
    }
  }
  run()
}

const deleteCategory = (id) => {
  const run = async () => {
    try {
      await request(`/api/categories/${id}`, { method: 'DELETE' })
      await loadCategories()
      if (Number(activeCategory.value) === id) {
        activeCategory.value = 'all'
      }
      await loadProducts()
    } catch (error) {
      console.error(error)
      alert('分类删除失败')
    }
  }
  run()
}

const submitRecord = () => {
  if (!recordForm.content.trim() || !recordForm.author.trim()) return

  const payload = {
    id: editingRecordId.value,
    type: recordForm.type,
    content: recordForm.content.trim(),
    author: recordForm.author.trim(),
    goodsId: null,
    time: new Date().toLocaleString('zh-CN', { hour12: false })
  }

  const run = async () => {
    try {
      if (editingRecordId.value) {
        await request(`/api/messages/${editingRecordId.value}`, {
          method: 'PUT',
          body: JSON.stringify(payload)
        })
      } else {
        await request('/api/messages', {
          method: 'POST',
          body: JSON.stringify(payload)
        })
      }
      await loadRecords()
      resetRecordForm()
    } catch (error) {
      console.error(error)
      alert('记录保存失败')
    }
  }
  run()
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
  const run = async () => {
    try {
      await request(`/api/messages/${id}`, { method: 'DELETE' })
      await loadRecords()
      if (editingRecordId.value === id) {
        resetRecordForm()
      }
    } catch (error) {
      console.error(error)
      alert('记录删除失败')
    }
  }
  run()
}

onMounted(() => {
  loadAll()
})

watch(activeCategory, () => {
  loadProducts()
})
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
          <article v-for="item in products" :key="item.id" class="card">
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

          <div v-if="products.length === 0" class="empty">
            当前分类暂无商品，试试新增一条。
          </div>
        </div>
      </section>
    </main>
  </div>
</template>
