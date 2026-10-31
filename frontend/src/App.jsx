import { useMemo, useState } from 'react'
import {
  ArrowDown,
  ArrowRight,
  Check,
  ChevronDown,
  Heart,
  Leaf,
  Menu,
  Minus,
  Plus,
  Search,
  ShoppingBag,
  Star,
  Truck,
  X,
} from 'lucide-react'

const products = [
  {
    id: 1,
    name: 'Farm Fresh Whole Milk',
    detail: 'Whole milk · 1 litre',
    price: 68,
    category: 'Milk',
    badge: 'BESTSELLER',
    image:
      'https://images.unsplash.com/photo-1563636619-e9143da7973b?auto=format&fit=crop&w=900&q=85',
    imageAlt: 'Fresh milk poured into a glass',
    color: 'bg-[#e6ede3]',
  },
  {
    id: 2,
    name: 'Cultured Greek Yogurt',
    detail: 'Plain · 400 g',
    price: 145,
    category: 'Yogurt',
    badge: 'HIGH PROTEIN',
    image:
      'https://images.unsplash.com/photo-1488477181946-6428a0291777?auto=format&fit=crop&w=900&q=85',
    imageAlt: 'Creamy yogurt topped with fresh berries',
    color: 'bg-[#f4e9dc]',
  },
  {
    id: 3,
    name: 'Country Cream Butter',
    detail: 'Hand-churned · 200 g',
    price: 120,
    category: 'Butter & Ghee',
    badge: '',
    image:
      'https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?auto=format&fit=crop&w=900&q=85',
    imageAlt: 'Golden butter on a wooden board',
    color: 'bg-[#f4edcf]',
  },
  {
    id: 4,
    name: 'Traditional Desi Ghee',
    detail: 'Bilona method · 500 ml',
    price: 549,
    category: 'Butter & Ghee',
    badge: 'SMALL BATCH',
    image:
      'https://images.unsplash.com/photo-1630409346824-4f0e7b080087?auto=format&fit=crop&w=900&q=85',
    imageAlt: 'Golden ghee served in a small bowl',
    color: 'bg-[#f2e7d0]',
  },
  {
    id: 5,
    name: 'Fresh Paneer',
    detail: 'Soft & creamy · 200 g',
    price: 95,
    category: 'Cheese',
    badge: 'MADE TODAY',
    image:
      'https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=900&q=85',
    imageAlt: 'Fresh paneer cubes with herbs',
    color: 'bg-[#eeece2]',
  },
  {
    id: 6,
    name: 'Creamy Cottage Cheese',
    detail: 'Farm style · 250 g',
    price: 130,
    category: 'Cheese',
    badge: '',
    image:
      'https://images.unsplash.com/photo-1559561853-08451507cbe7?auto=format&fit=crop&w=900&q=85',
    imageAlt: 'A creamy cheese dish served fresh',
    color: 'bg-[#e9ede4]',
  },
  {
    id: 7,
    name: 'Thick Farm Curd',
    detail: 'Naturally set · 500 g',
    price: 75,
    category: 'Yogurt',
    badge: '',
    image:
      'https://images.unsplash.com/photo-1571212515416-fca9d3e0f6b6?auto=format&fit=crop&w=900&q=85',
    imageAlt: 'A bowl of fresh, creamy curd',
    color: 'bg-[#e5ebdf]',
  },
  {
    id: 8,
    name: 'Fresh Cream',
    detail: 'Rich & smooth · 200 ml',
    price: 90,
    category: 'Milk',
    badge: '',
    image:
      'https://images.unsplash.com/photo-1628088062854-d1870b4553da?auto=format&fit=crop&w=900&q=85',
    imageAlt: 'A glass bottle of fresh dairy milk',
    color: 'bg-[#f4e9dc]',
  },
]

const categories = ['All products', 'Milk', 'Yogurt', 'Butter & Ghee', 'Cheese']

function App() {
  const [activeCategory, setActiveCategory] = useState('All products')
  const [search, setSearch] = useState('')
  const [cart, setCart] = useState({})
  const [cartOpen, setCartOpen] = useState(false)
  const [menuOpen, setMenuOpen] = useState(false)
  const [notice, setNotice] = useState('')

  const visibleProducts = useMemo(() => {
    const query = search.trim().toLowerCase()
    return products.filter((product) => {
      const matchesCategory =
        activeCategory === 'All products' || product.category === activeCategory
      const matchesSearch =
        !query ||
        `${product.name} ${product.detail} ${product.category}`
          .toLowerCase()
          .includes(query)
      return matchesCategory && matchesSearch
    })
  }, [activeCategory, search])

  const cartItems = products.filter((product) => cart[product.id])
  const cartCount = Object.values(cart).reduce((total, quantity) => total + quantity, 0)
  const subtotal = cartItems.reduce(
    (total, product) => total + product.price * cart[product.id],
    0,
  )

  function updateCart(productId, amount) {
    setCart((currentCart) => {
      const nextQuantity = (currentCart[productId] || 0) + amount
      const nextCart = { ...currentCart }
      if (nextQuantity <= 0) delete nextCart[productId]
      else nextCart[productId] = nextQuantity
      return nextCart
    })
  }

  function addToCart(product) {
    updateCart(product.id, 1)
    setNotice(`${product.name} added to your bag`)
    window.setTimeout(() => setNotice(''), 2400)
  }

  return (
    <div className="min-h-screen overflow-hidden bg-cream">
      <div className="bg-forest px-4 py-2.5 text-center text-[11px] font-semibold tracking-[0.13em] text-white sm:text-xs">
        A LITTLE FARM GOODNESS, DELIVERED FRESH — FREE DELIVERY ON ORDERS OVER ₹499
      </div>

      <header className="relative z-20 border-b border-forest/10 bg-cream">
        <div className="mx-auto flex h-[78px] max-w-[1320px] items-center justify-between px-5 sm:px-8 lg:px-12">
          <a className="flex items-center gap-2.5" href="#" aria-label="Arunayan Dairy Store home">
            <span className="grid h-10 w-10 place-items-center rounded-full bg-forest text-butter">
              <Leaf size={21} strokeWidth={1.8} />
            </span>
            <span className="leading-tight">
              <span className="block font-display text-[20px] font-semibold tracking-[-0.04em] text-forest">
                arunayan
              </span>
              <span className="block text-[9px] font-bold uppercase tracking-[0.24em] text-leaf">
                Dairy Store
              </span>
            </span>
          </a>

          <nav className="hidden items-center gap-9 text-[13px] font-medium text-forest/80 md:flex">
            <a className="transition hover:text-leaf" href="#shop">Shop all</a>
            <a className="transition hover:text-leaf" href="#story">Our story</a>
            <a className="transition hover:text-leaf" href="#promise">Our promise</a>
          </nav>

          <div className="flex items-center gap-2.5">
            <label className="hidden h-10 items-center gap-2 rounded-full border border-forest/15 px-3.5 lg:flex">
              <Search size={16} className="text-forest/55" />
              <input
                aria-label="Search products"
                className="w-28 bg-transparent text-xs outline-none placeholder:text-forest/45"
                onChange={(event) => {
                  setSearch(event.target.value)
                  document.getElementById('shop')?.scrollIntoView({ behavior: 'smooth' })
                }}
                placeholder="Find something..."
                value={search}
              />
            </label>
            <button
              aria-label={`Open shopping bag, ${cartCount} items`}
              className="relative grid h-10 w-10 place-items-center rounded-full border border-forest/15 transition hover:bg-white"
              onClick={() => setCartOpen(true)}
            >
              <ShoppingBag size={18} />
              {cartCount > 0 && (
                <span className="absolute -right-1 -top-1 grid h-[18px] min-w-[18px] place-items-center rounded-full bg-butter px-1 text-[10px] font-bold">
                  {cartCount}
                </span>
              )}
            </button>
            <button
              aria-label={menuOpen ? 'Close menu' : 'Open menu'}
              className="grid h-10 w-10 place-items-center rounded-full border border-forest/15 md:hidden"
              onClick={() => setMenuOpen(!menuOpen)}
            >
              {menuOpen ? <X size={18} /> : <Menu size={18} />}
            </button>
          </div>
        </div>
        {menuOpen && (
          <nav className="absolute left-0 right-0 top-full flex flex-col gap-4 border-t border-forest/10 bg-cream px-6 py-5 text-sm md:hidden">
            <a href="#shop" onClick={() => setMenuOpen(false)}>Shop all</a>
            <a href="#story" onClick={() => setMenuOpen(false)}>Our story</a>
            <a href="#promise" onClick={() => setMenuOpen(false)}>Our promise</a>
          </nav>
        )}
      </header>

      <main>
        <section className="mx-auto max-w-[1320px] px-4 pt-5 sm:px-8 lg:px-12 lg:pt-8">
          <div className="hero-image relative flex min-h-[480px] items-center overflow-hidden rounded-[24px] bg-cover bg-center px-7 py-12 sm:min-h-[550px] sm:px-14 lg:min-h-[600px] lg:px-[84px]">
            <div className="relative z-10 max-w-[530px] text-white">
              <span className="mb-6 inline-flex items-center gap-2 rounded-full border border-white/35 bg-white/10 px-3.5 py-2 text-[10px] font-semibold uppercase tracking-[0.17em] backdrop-blur-sm">
                <span className="h-1.5 w-1.5 rounded-full bg-butter" />
                From our farms to your table
              </span>
              <h1 className="font-display text-[48px] font-medium leading-[1.05] tracking-[-0.045em] sm:text-[64px] lg:text-[74px]">
                Goodness,
                <br />
                <span className="italic text-[#f3d58c]">delivered</span> fresh.
              </h1>
              <p className="mt-5 max-w-[390px] text-sm leading-6 text-white/85 sm:text-base sm:leading-7">
                Honest dairy from happy farms. Made with care, delivered with love — every single day.
              </p>
              <a
                className="mt-8 inline-flex h-12 items-center gap-3 rounded-full bg-butter px-6 text-[13px] font-bold text-forest transition hover:bg-[#f7d987]"
                href="#shop"
              >
                Shop the goodness <ArrowRight size={16} />
              </a>
              <div className="mt-12 flex items-center gap-3 text-xs text-white/80">
                <span className="flex -space-x-2">
                  {['photo-1534528741775-53994a69daeb', 'photo-1500648767791-00dcc994a43e', 'photo-1531123897727-8f129e1688ce'].map((photo) => (
                    <img
                      alt=""
                      className="h-8 w-8 rounded-full border-2 border-white/80 object-cover"
                      key={photo}
                      src={`https://images.unsplash.com/${photo}?auto=format&fit=crop&w=80&h=80&q=75`}
                    />
                  ))}
                </span>
                <span><strong className="font-bold text-white">2,400+</strong> happy homes</span>
                <span className="ml-1 flex items-center gap-1 text-butter"><Star size={13} fill="currentColor" /> 4.9</span>
              </div>
            </div>
            <div className="absolute bottom-8 right-8 hidden h-[132px] w-[132px] rotate-[-9deg] flex-col items-center justify-center rounded-full border border-white/55 bg-white/90 text-center text-forest shadow-xl backdrop-blur-sm md:flex">
              <span className="font-display text-3xl leading-none">100%</span>
              <span className="mt-1 text-[9px] font-bold uppercase tracking-[0.16em]">Farm fresh</span>
              <span className="mt-1 text-[9px] uppercase tracking-[0.1em]">every morning</span>
            </div>
            <a aria-label="Scroll to shop" className="absolute bottom-7 left-1/2 hidden -translate-x-1/2 text-white/80 transition hover:text-white sm:block" href="#shop">
              <ArrowDown size={20} />
            </a>
          </div>
        </section>

        <section className="mx-auto grid max-w-[1320px] grid-cols-2 gap-y-7 px-6 py-10 sm:grid-cols-4 sm:px-8 sm:py-12 lg:px-16" id="promise">
          {[
            { icon: Leaf, title: 'Farm to doorstep', text: 'Sourced with care' },
            { icon: Check, title: 'Nothing artificial', text: 'Just honest goodness' },
            { icon: Truck, title: 'Fresh every day', text: 'At your door by 7 AM' },
            { icon: Heart, title: 'Made with love', text: 'From our family to yours' },
          ].map(({ icon: Icon, title, text }) => (
            <div className="flex items-center gap-3 sm:justify-center" key={title}>
              <span className="grid h-11 w-11 shrink-0 place-items-center rounded-full bg-[#e8ede3] text-leaf">
                <Icon size={19} strokeWidth={1.7} />
              </span>
              <span>
                <span className="block text-[12px] font-bold sm:text-[13px]">{title}</span>
                <span className="mt-0.5 block text-[11px] text-forest/55 sm:text-xs">{text}</span>
              </span>
            </div>
          ))}
        </section>

        <section className="bg-white py-16 sm:py-20" id="shop">
          <div className="mx-auto max-w-[1320px] px-5 sm:px-8 lg:px-12">
            <div className="mb-8 flex flex-col justify-between gap-5 sm:mb-10 sm:flex-row sm:items-end">
              <div>
                <span className="text-[10px] font-bold uppercase tracking-[0.2em] text-leaf">A little something for everyone</span>
                <h2 className="mt-2 font-display text-4xl font-medium tracking-[-0.04em] sm:text-[46px]">The daily good stuff.</h2>
                <p className="mt-2 text-sm text-forest/60">Small-batch dairy, made fresh and delivered to you.</p>
              </div>
              <a className="hidden items-center gap-2 pb-1 text-xs font-bold transition hover:text-leaf sm:inline-flex" href="#shop">
                Explore everything <ArrowRight size={15} />
              </a>
            </div>

            <div className="mb-8 flex flex-wrap items-center gap-2.5">
              {categories.map((category) => (
                <button
                  aria-pressed={activeCategory === category}
                  className={`rounded-full border px-4 py-2.5 text-xs font-semibold transition ${
                    activeCategory === category
                      ? 'border-forest bg-forest text-white'
                      : 'border-forest/15 text-forest/70 hover:border-forest/40'
                  }`}
                  key={category}
                  onClick={() => setActiveCategory(category)}
                >
                  {category}
                </button>
              ))}
              <label className="ml-auto flex h-10 w-full items-center gap-2 rounded-full border border-forest/15 px-3.5 sm:hidden">
                <Search size={15} className="text-forest/55" />
                <input
                  aria-label="Search products"
                  className="w-full bg-transparent text-xs outline-none placeholder:text-forest/45"
                  onChange={(event) => setSearch(event.target.value)}
                  placeholder="Search products..."
                  value={search}
                />
              </label>
              <span className="hidden items-center gap-1.5 text-xs text-forest/55 sm:ml-auto sm:flex">
                Sort by <ChevronDown size={14} />
              </span>
            </div>

            {visibleProducts.length > 0 ? (
              <div className="grid grid-cols-2 gap-x-4 gap-y-7 sm:grid-cols-3 sm:gap-x-5 lg:grid-cols-4 lg:gap-x-6">
                {visibleProducts.map((product) => (
                  <article className="product-card group min-w-0" key={product.id}>
                    <div className={`relative aspect-[0.92] overflow-hidden rounded-[14px] ${product.color}`}>
                      <img
                        alt={product.imageAlt}
                        className="product-image h-full w-full object-cover"
                        loading="lazy"
                        src={product.image}
                      />
                      {product.badge && (
                        <span className="absolute left-3 top-3 rounded-full bg-white/90 px-2.5 py-1.5 text-[8px] font-bold tracking-[0.12em] text-forest sm:text-[9px]">
                          {product.badge}
                        </span>
                      )}
                      <button
                        aria-label={`Add ${product.name} to bag`}
                        className="absolute bottom-3 right-3 grid h-10 w-10 place-items-center rounded-full bg-white text-forest shadow-sm transition hover:bg-forest hover:text-white sm:opacity-0 sm:group-hover:opacity-100"
                        onClick={() => addToCart(product)}
                      >
                        <Plus size={18} />
                      </button>
                    </div>
                    <div className="flex items-start justify-between gap-2 pt-3">
                      <div className="min-w-0">
                        <h3 className="truncate text-[12px] font-bold sm:text-sm">{product.name}</h3>
                        <p className="mt-1 text-[10px] text-forest/55 sm:text-xs">{product.detail}</p>
                      </div>
                      <span className="shrink-0 text-xs font-bold sm:text-sm">₹{product.price}</span>
                    </div>
                    <button
                      className="mt-3 flex h-9 w-full items-center justify-center gap-2 rounded-full border border-forest/20 text-[11px] font-semibold transition hover:border-forest hover:bg-forest hover:text-white sm:hidden"
                      onClick={() => addToCart(product)}
                    >
                      <Plus size={14} /> Add to bag
                    </button>
                  </article>
                ))}
              </div>
            ) : (
              <div className="rounded-2xl bg-cream px-6 py-16 text-center">
                <p className="font-display text-2xl">Nothing in the churn just yet.</p>
                <p className="mt-2 text-sm text-forest/60">Try another search or browse all of our fresh picks.</p>
                <button
                  className="mt-5 rounded-full bg-forest px-5 py-2.5 text-xs font-bold text-white"
                  onClick={() => {
                    setSearch('')
                    setActiveCategory('All products')
                  }}
                >
                  Show all products
                </button>
              </div>
            )}
          </div>
        </section>

        <section className="mx-auto grid max-w-[1320px] gap-8 px-5 py-16 sm:px-8 sm:py-20 lg:grid-cols-2 lg:items-center lg:gap-16 lg:px-16" id="story">
          <div className="relative">
            <img
              alt="A peaceful dairy farm surrounded by green fields"
              className="h-[340px] w-full rounded-[20px] object-cover sm:h-[440px]"
              loading="lazy"
              src="https://images.unsplash.com/photo-1500382017468-9049fed747ef?auto=format&fit=crop&w=1200&q=85"
            />
            <div className="absolute -bottom-5 right-4 rounded-xl bg-butter px-5 py-4 shadow-lg sm:right-[-18px]">
              <p className="font-display text-2xl">Since 1998</p>
              <p className="mt-0.5 text-[10px] font-semibold uppercase tracking-[0.12em]">Rooted in goodness</p>
            </div>
          </div>
          <div className="pt-4 lg:pt-0">
            <span className="text-[10px] font-bold uppercase tracking-[0.2em] text-leaf">A little more about us</span>
            <h2 className="mt-3 font-display text-[38px] font-medium leading-[1.1] tracking-[-0.04em] sm:text-[52px]">
              Good dairy starts with good care.
            </h2>
            <p className="mt-5 max-w-lg text-sm leading-7 text-forest/65">
              We believe the best things are made slowly, thoughtfully, and close to home. That&apos;s why we work with local farmers who care for their animals and their land as much as we care about what reaches your table.
            </p>
            <a className="mt-7 inline-flex items-center gap-2 border-b border-forest pb-1.5 text-xs font-bold transition hover:gap-3" href="#promise">
              Get to know our promise <ArrowRight size={15} />
            </a>
          </div>
        </section>

        <section className="bg-[#e9eee5] px-5 py-14 text-center sm:px-8 sm:py-16">
          <span className="text-[10px] font-bold uppercase tracking-[0.2em] text-leaf">Goodness in your inbox</span>
          <h2 className="mt-3 font-display text-3xl tracking-[-0.04em] sm:text-4xl">A little love from the farm.</h2>
          <p className="mx-auto mt-2 max-w-md text-sm leading-6 text-forest/60">Fresh updates, seasonal goodies, and 10% off your first order. No spam, just the good stuff.</p>
          <form
            className="mx-auto mt-6 flex max-w-[420px] rounded-full border border-forest/20 bg-white p-1.5"
            onSubmit={(event) => {
              event.preventDefault()
              setNotice('You’re on the list — keep an eye on your inbox!')
              window.setTimeout(() => setNotice(''), 3000)
              event.currentTarget.reset()
            }}
          >
            <input aria-label="Email address" className="min-w-0 flex-1 bg-transparent px-4 text-xs outline-none placeholder:text-forest/45" placeholder="Your email address" required type="email" />
            <button className="flex shrink-0 items-center gap-2 rounded-full bg-forest px-4 py-2.5 text-[11px] font-bold text-white transition hover:bg-leaf sm:px-5">
              Count me in <ArrowRight size={14} />
            </button>
          </form>
        </section>
      </main>

      <footer className="bg-forest px-5 py-10 text-white sm:px-8 sm:py-12">
        <div className="mx-auto flex max-w-[1320px] flex-col justify-between gap-8 sm:flex-row sm:items-end lg:px-4">
          <div>
            <a className="inline-flex items-center gap-2.5" href="#">
              <span className="grid h-10 w-10 place-items-center rounded-full bg-white/10 text-butter"><Leaf size={20} /></span>
              <span>
                <span className="block font-display text-xl">arunayan</span>
                <span className="block text-[9px] font-bold uppercase tracking-[0.24em] text-white/60">Dairy Store</span>
              </span>
            </a>
            <p className="mt-4 max-w-xs text-xs leading-5 text-white/60">Honest dairy, happy farms, and a little more goodness in every day.</p>
          </div>
          <div className="flex flex-wrap gap-x-6 gap-y-3 text-xs text-white/75">
            <a className="hover:text-white" href="#shop">Shop</a>
            <a className="hover:text-white" href="#story">Our story</a>
            <a className="hover:text-white" href="#promise">Our promise</a>
            <a className="hover:text-white" href="mailto:hello@arunayandairy.com">Get in touch</a>
          </div>
          <p className="text-[10px] text-white/45">© 2025 Arunayan Dairy Store. Made with care.</p>
        </div>
      </footer>

      {notice && (
        <div className="fixed bottom-5 left-1/2 z-50 -translate-x-1/2 rounded-full bg-forest px-5 py-3 text-xs font-semibold text-white shadow-xl" role="status">
          {notice}
        </div>
      )}

      {cartOpen && (
        <div className="fixed inset-0 z-40">
          <button aria-label="Close shopping bag" className="absolute inset-0 bg-forest/35" onClick={() => setCartOpen(false)} />
          <aside aria-label="Shopping bag" aria-modal="true" className="absolute bottom-0 right-0 top-0 flex w-full max-w-[430px] flex-col bg-cream shadow-2xl" role="dialog">
            <div className="flex items-center justify-between border-b border-forest/10 px-6 py-5">
              <div>
                <h2 className="font-display text-2xl">Your bag</h2>
                <p className="mt-1 text-xs text-forest/55">{cartCount} {cartCount === 1 ? 'good thing' : 'good things'}</p>
              </div>
              <button aria-label="Close shopping bag" className="grid h-9 w-9 place-items-center rounded-full border border-forest/15" onClick={() => setCartOpen(false)}><X size={17} /></button>
            </div>
            {cartItems.length ? (
              <>
                <div className="flex-1 space-y-4 overflow-y-auto p-6">
                  {cartItems.map((product) => (
                    <div className="flex gap-4" key={product.id}>
                      <img alt={product.imageAlt} className="h-20 w-20 rounded-xl object-cover" src={product.image} />
                      <div className="min-w-0 flex-1">
                        <div className="flex justify-between gap-2">
                          <div>
                            <p className="text-sm font-bold">{product.name}</p>
                            <p className="mt-1 text-xs text-forest/55">{product.detail}</p>
                          </div>
                          <span className="text-sm font-bold">₹{product.price * cart[product.id]}</span>
                        </div>
                        <div className="mt-3 inline-flex items-center gap-3 rounded-full border border-forest/15 px-2.5 py-1.5">
                          <button aria-label={`Remove one ${product.name}`} onClick={() => updateCart(product.id, -1)}><Minus size={13} /></button>
                          <span className="text-xs font-semibold">{cart[product.id]}</span>
                          <button aria-label={`Add one ${product.name}`} onClick={() => updateCart(product.id, 1)}><Plus size={13} /></button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
                <div className="border-t border-forest/10 p-6">
                  <div className="mb-4 flex justify-between text-sm"><span>Subtotal</span><strong>₹{subtotal}</strong></div>
                  <p className="mb-4 text-[11px] text-forest/55">Shipping and taxes calculated at checkout.</p>
                  <button className="flex h-12 w-full items-center justify-center gap-2 rounded-full bg-forest text-sm font-bold text-white transition hover:bg-leaf" onClick={() => setNotice('Checkout is coming soon!')}>
                    Continue to checkout <ArrowRight size={16} />
                  </button>
                </div>
              </>
            ) : (
              <div className="flex flex-1 flex-col items-center justify-center px-8 text-center">
                <span className="grid h-16 w-16 place-items-center rounded-full bg-[#e8ede3] text-leaf"><ShoppingBag size={25} /></span>
                <h3 className="mt-5 font-display text-2xl">Your bag is taking a breather.</h3>
                <p className="mt-2 text-sm text-forest/55">Let&apos;s fill it with something fresh.</p>
                <button className="mt-6 rounded-full bg-forest px-6 py-3 text-xs font-bold text-white" onClick={() => setCartOpen(false)}>Find the good stuff</button>
              </div>
            )}
          </aside>
        </div>
      )}
    </div>
  )
}

export default App
