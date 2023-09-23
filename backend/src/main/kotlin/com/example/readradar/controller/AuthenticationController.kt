package com.example.readradar.controller

import com.example.readradar.configuration.JwtUtil
import com.example.readradar.model.Category
import com.example.readradar.model.Role
import com.example.readradar.model.dto.*
import com.example.readradar.repository.RoleRepository
import com.example.readradar.service.BookService
import com.example.readradar.service.CategoryService
import com.example.readradar.service.MyUserDetailsService
import com.example.readradar.service.ReviewService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val userDetailsService: MyUserDetailsService,
    private val roleRepository: RoleRepository,
    private val bookService: BookService,
    private val categoryService: CategoryService,
    private val reviewService: ReviewService
) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginDTO): ResponseEntity<Any> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = userDetailsService.loadUserByUsername(loginRequest.username)
        val jwt = jwtUtil.generateToken(userDetails!!.username)
        jwtUtil.validateToken(jwt, userDetails.username)
        val roles: List<String> = userDetails.authorities.stream()
            .map { obj: GrantedAuthority? -> obj!!.authority }!!.toList()
        val loginResponse = LoginResponse(jwt, userDetails.username, roles[0])
        return ResponseEntity.ok().body(loginResponse)
    }

    @PostMapping("/logout")
    fun logout(): ResponseEntity<Any> {
        return ResponseEntity.ok()
            .body(("You have been logged out"))
    }

    @PostMapping("/register")
    fun register(@RequestBody registerDTO: RegisterDTO): ResponseEntity<Any> {
        return ResponseEntity.ok(userDetailsService.register(registerDTO))
    }

    @PostMapping("/init-database")
    fun initDatabase(): ResponseEntity<Any> {
        roleRepository.saveAll(
            listOf(
                Role(1, "USER"),
                Role(2, "ADMIN")
            )
        )

        userDetailsService.register(
            RegisterDTO(
                username = "test",
                password = "test",
                email = "test@mail.com",
                profilePicture = "https://i.imgur.com/4oA1WVJ.png",
                roleId = 1
            )
        )

        userDetailsService.register(
            RegisterDTO(
                username = "admin",
                password = "admin",
                email = "admin@mail.com",
                profilePicture = "https://i.imgur.com/4oA1WVJ.png",
                roleId = 2
            )
        )

        categoryService.save(
            Category(
                id = 1,
                name = "Romance"
            )
        )
        categoryService.save(
            Category(
                id = 2,
                name = "Biography"
            )
        )
        categoryService.save(
            Category(
                id = 3,
                name = "Children's Books"
            )
        )


        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "It Starts with Us",
                isbn = "9781668001226",
                author = "Colleen Hoover",
                coverImage = "https://www3.alibris-static.com/it-starts-with-us/isbn/9781668001226_l.jpg",
                description = "Lily and her ex-husband, Ryle, have just settled into a civil coparenting rhythm when she suddenly bumps into her first love, Atlas, again. After nearly two years separated, she is elated that for once, time is on their side, and she immediately says yes when Atlas asks her on a date. But her excitement is quickly hampered by the knowledge that, though they are no longer married, Ryle is still very much a part of her life--and Atlas Corrigan is the one man he will hate being in his ex-wife and daughter's life. ",
                categoryIds = listOf(1)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "It Ends with Us",
                isbn = "9781501110368",
                author = "Colleen Hoover",
                coverImage = "https://www3.alibris-static.com/it-ends-with-us/isbn/9781501110368_l.jpg",
                description = "Lily hasn't always had it easy, but that's never stopped her from working hard for the life she wants. She's come a long way from the small town in Maine where she grew up--she graduated from college, moved to Boston, and started her own business. So when she feels a spark with a gorgeous neurosurgeon named Ryle Kincaid, everything in Lily's life suddenly seems almost too good to be true ... But Ryle's complete aversion to relationships is disturbing. Even as Lily finds herself becoming the exception to his 'no dating' rule, she can't help but wonder what made him that way in the first place",
                categoryIds = listOf(1)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "Too Late: Definitive Edition",
                isbn = "9781538756591",
                author = "Colleen Hoover",
                coverImage = "https://www0.alibris-static.com/too-late-definitive-edition/isbn/9781538756591_l.jpg",
                description = "Sloan will go through hell and back for those she loves. And she does so, every single day. Caught up with the alluring Asa Jackson, a notorious drug trafficker, Sloan has finally found a lifeline to cling to, even if it's meant compromising her morals. She was in dire straits trying to pay for her brother's care until she met Asa. But as Sloan became emotionally and economically reliant on him, he in turn developed a disturbing obsession with her -- one that becomes increasingly dangerous every day. When undercover DEA agent Carter enters the picture, Sloan's surprised to feel an immediate attraction between them, despite knowing that if Asa finds out, he will kill him. And Asa has always been a step ahead of everyone in his life, including Sloan. No one has ever gotten in his way. No one except Carter. Together, Sloan and Carter must find a way out before it's too late..",
                categoryIds = listOf(1)
            )
        )

        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "Things We Never Got Over",
                isbn = "9781945631832",
                author = "Lucy Score",
                coverImage = "https://www2.alibris-static.com/things-we-never-got-over/isbn/9781945631832_l.jpg",
                description = "Bearded, bad-boy barber Knox prefers to live his life the way he takes his coffee: alone--unless you count his basset hound Waylon. Knox doesn't tolerate drama, even when it comes in the form of a stranded runaway bride. Naomi wasn't just running away from her wedding. She was riding to the rescue of her estranged twin to Knockemout, Virginia, a rough-around-the-edges town where disputes are settled the old-fashioned way: with fists and beer. Usually in that order. Too bad for Naomi her evil twin hasn't changed at all. After helping herself to Naomi's car and cash, Tina leaves her with something unexpected: the niece Naomi didn't know she had",
                categoryIds = listOf(1)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "Elon Musk",
                isbn = "9781982181284",
                author = "Walter Isaacson",
                coverImage = "https://www2.alibris-static.com/elon-musk/isbn/9781982181284_l.jpg",
                description = "Epic feats. Epic failures. An epic story. Walter Isaacson charts Elon Musk's journey from humble beginnings to one of the wealthiest people on the planet - but is Musk a genius or a jerk?",
                categoryIds = listOf(2)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "Spare",
                isbn = "9780593593806",
                author = "Prince Harry the Duke of Sussex",
                coverImage = "https://www2.alibris-static.com/spare/isbn/9780593593806_l.jpg",
                description = "It was one of the most searing images of the twentieth century: two young boys, two princes, walking behind their mother's coffin as the world watched in sorrow and horror. As Diana, Princess of Wales, was laid to rest, billions wondered what the princes must be thinking and feeling, and how their lives would play out from that point on. For Harry, this is that story at last. With its raw, unflinching honesty, Prince Harry shares a landmark publication full of insight, revelation, self-examination, and hard-won wisdom about the eternal power of love over grief.-- From jacket flap",
                categoryIds = listOf(2)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "The Mamba Mentality: How I Play",
                isbn = "9780374201234",
                author = "Kobe Bryant",
                coverImage = "https://www2.alibris-static.com/the-mamba-mentality-how-i-play/isbn/9780374201234_l.jpg",
                description = "The first book from the basketball superstar Kobe Bryant--a lavish, deep dive inside the mind of one of the most revered athletes of all time",
                categoryIds = listOf(2)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "The Light We Carry: Overcoming in Uncertain Times",
                isbn = "9780593237465",
                author = "Michelle Obama",
                coverImage = "https://www0.alibris-static.com/the-light-we-carry-overcoming-in-uncertain-times/isbn/9780593237465_l.jpg",
                description = "The powerful, inspiring follow-up to the critically acclaimed, multi-million #1 bestselling memoir Becoming In The Light We Carry, former First Lady Michelle Obama shares practical wisdom and powerful strategies for staying hopeful and balanced in today's highly uncertain world. She considers the questions many of us wrestle with: How do we build enduring and honest relationships? How can we discover strength and community inside our differences? What do we do when it all starts to feel like too much? Michelle Obama believes that we can all lean on a set of tools to help us better navigate change and remain steady within flux. The Light We Carry offers readers a series of fresh stories and insightful reflections on change, challenge, and power, including her belief that when we light up for others, we can illuminate the richness and potential of the world around us, discovering deeper truths and new pathways for progress. Drawing from her experiences as a mother, daughter, spouse, friend, and First Lady, she shares the habits and principles she has developed to successfully overcome various obstacles-the earned wisdom that helps her continue to \"become.\" With trademark humour, candour, and compassion, she also explores issues connected to race, gender, and visibility, encouraging readers to work through fear, find strength in community, and live with boldness. The Light We Carry will inspire readers to examine their own lives, identify their sources of gladness, and connect meaningfully in a turbulent world. Praise for Becoming: 'An inspirational memoir that also rings true' -- Daily Telegraph 'This is a rich, entertaining and candid memoir...it is as beautifully written as any piece of fiction' -- i 'In the best moments of Becoming, the miracle of Michelle Obama arises' -- Vanity Fair 'Intimate, inspiring and set to become hugely influential' -- Sunday Times 'Becoming serenely balances gravity and grace, uplift and anecdote' -- Observer 'What a memoir. What a woman'",
                categoryIds = listOf(2)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "Spooky Pookie",
                isbn = "9780553512335",
                author = "Sandra Boynton",
                coverImage = "https://www4.alibris-static.com/spooky-pookie/isbn/9780553512335_l.jpg",
                description = "It's Halloween and Pookie is trying on costumes one by one, but somehow can't find just the right thing. The resolution to Pookie's dilemma will delight toddlers and their caregivers alike. Full color.",
                categoryIds = listOf(3)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "I Love You to the Moon and Back",
                isbn = "9781589255517",
                author = "Amelia Hepworth",
                coverImage = "https://www4.alibris-static.com/i-love-you-to-the-moon-and-back/isbn/9781801041508_l.jpg",
                description = "So snuggle safely in my arms; our day is nearly done. I love you to the moon and stars, my precious little one. A beautiful hardback gift edition of the international best-seller I Love You to the Moon and Back, a bedtime favourite with familiar and reassuring text by Amelia Hepworth and heartwarming illustrations by Tim Warnes. When the sun comes up, Big Bear and Little Bear think of new ways to share their love. Big Bear loves Little Bear more and more as each day passes, right up to each new moon - and back. Now this joyful celebration of the love between a parent and child can be treasured forever with this elegant cloth-textured, silver foil and peep-through cover edition - the perfect gift for a special person in your life",
                categoryIds = listOf(3)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "Dragons Love Tacos",
                isbn = "9780803736801",
                author = "Adam Rubin",
                coverImage = "https://www4.alibris-static.com/dragons-love-tacos/isbn/9780803736801_l.jpg",
                description = "Hooray, you're hosting your very first party for dragons! Dragons love tacos. And if you have plenty of tacos, nothing could possibly go wrong at your party ... right?",
                categoryIds = listOf(3)
            )
        )
        bookService.saveOrUpdate(
            CreateBookDTO(
                title = "You're My Little Pumpkin Pie",
                isbn = "9781684124343",
                author = "Natalie Marshall",
                coverImage = "https://www3.alibris-static.com/youre-my-little-pumpkin-pie/isbn/9781684124343_l.jpg",
                description = "With adorable illustrations, shaped cutouts, and raised elements, this sweet board book is ideal for sharing with little ones. Full color",
                categoryIds = listOf(3)
            )
        )
        reviewService.save(
            AddReviewDTO(
                4.0, "Great book, I really enjoyed it",
            ), 1, 1
        )
        reviewService.save(
            AddReviewDTO(
                5.0, "I loved this book, it was amazing",
            ), 1, 2
        )
        reviewService.save(
            AddReviewDTO(
                3.0, "It was ok, I guess",
            ), 1, 3
        )
        reviewService.save(
            AddReviewDTO(
                4.0, "I really liked this book",
            ), 1, 4
        )
        reviewService.save(
            AddReviewDTO(
                5.0, "I loved this book, it was amazing",
            ), 1, 5
        )
        reviewService.save(
            AddReviewDTO(
                4.0, "I really liked this book",
            ), 1, 6
        )
        reviewService.save(
            AddReviewDTO(
                5.0, "I loved this book, it was amazing",
            ), 1, 7
        )
        reviewService.save(
            AddReviewDTO(
                4.0, "I really liked this book",
            ), 1, 8
        )
        reviewService.save(
            AddReviewDTO(
                5.0, "I loved this book, it was amazing",
            ), 1, 9
        )


        return ResponseEntity.ok().body("Database initialized")
    }
}
