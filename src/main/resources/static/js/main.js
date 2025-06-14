document.addEventListener("DOMContentLoaded", () => {
  // Mobile menu toggle
  const menuToggle = document.querySelector(".menu-toggle")
  const nav = document.querySelector(".nav")

  if (menuToggle && nav) {
    menuToggle.addEventListener("click", () => {
      nav.classList.toggle("active")
    })
  }

  // Smooth scrolling for anchor links
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault()

      const targetId = this.getAttribute("href")
      if (targetId === "#") return

      const targetElement = document.querySelector(targetId)
      if (targetElement) {
        window.scrollTo({
          top: targetElement.offsetTop - 70,
          behavior: "smooth",
        })
      }
    })
  })

  // Simple testimonial slider
  let currentTestimonial = 0
  const testimonials = [
    {
      text: "An amazing experience with HamroYatra. The guides were knowledgeable and the itinerary was perfect!",
      author: "Sarah Johnson",
      trip: "Trekked to Everest Base Camp",
    },
    {
      text: "HamroYatra made our family trip to Nepal unforgettable. Everything was well organized and hassle-free.",
      author: "David Miller",
      trip: "Cultural Tour of Kathmandu Valley",
    },
    {
      text: "The best travel agency in Nepal! They took care of everything and the guides were exceptional.",
      author: "Michael Chen",
      trip: "Annapurna Circuit Trek",
    },
  ]

  const testimonialContainer = document.querySelector(".testimonial")

  if (testimonialContainer) {
    setInterval(() => {
      currentTestimonial = (currentTestimonial + 1) % testimonials.length
      updateTestimonial()
    }, 5000)

    function updateTestimonial() {
      const current = testimonials[currentTestimonial]
      testimonialContainer.innerHTML = `
                <p>"${current.text}"</p>
                <div class="testimonial-author">
                    <h4>${current.author}</h4>
                    <p>${current.trip}</p>
                </div>
            `
    }
  }
})
