import React, { useRef, useState, useEffect } from 'react';
// Import Swiper React components
import { Swiper, SwiperSlide } from 'swiper/react';

// Import Swiper styles
import 'swiper/css';
import 'swiper/css/pagination';
import 'swiper/css/navigation';

// import required modules
import { Autoplay, Pagination, Navigation } from 'swiper/modules';
import '../styles/home.css';

function Home() {
    const [events, setEvents] = useState([]);
    const progressCircle = useRef(null);
    const progressContent = useRef(null);
    const onAutoplayTimeLeft = (s, time, progress) => {
        progressCircle.current.style.setProperty('--progress', 1 - progress);
        progressContent.current.textContent = `${Math.ceil(time / 1000)}s`;
    };

    useEffect(() => {
        fetch('/api/events')
            .then(response => response.json())
            .then(data => {
                setEvents(data);
            })
            .catch(error => console.error('Error fetching events:', error));
    }, []);

    return (
        <div className="home">
            <section className="banner">
                <div className="home-intro">
                    <span>Star Spot</span>
                    <div>
                        {/*<icon></icon>*/}
                        {/*<a href="/map1">*/}
                        {/*    This is a test link to MapPage1*/}
                        {/*</a>*/}
                    </div>
                </div>
                <div className = "logo">
                    <img src="/assets/logo-removebg.png" alt="logo"/>
                </div>
            </section>
            <section className="event-intro">
                <Swiper
                    spaceBetween={30}
                    centeredSlides={true}
                    autoplay={{
                        delay: 2500,
                        disableOnInteraction: false,
                    }}
                    pagination={{
                        clickable: true,
                    }}
                    navigation={true}
                    modules={[Autoplay, Pagination, Navigation]}
                    onAutoplayTimeLeft={onAutoplayTimeLeft}
                    className="mySwiper"
                >
                    <SwiperSlide>
                        <div className="event-intro">
                            <img src='/assets/meteor-intro.png' alt="intro"/>
                            <div className="event-intro-item-content">
                                <h1>Meteor Shower Introduction</h1>
                                <p>A meteor shower is a spectacular astronomical event where many meteors streak across
                                    the sky in a short period. It happens when Earth passes through a trail of debris
                                    left by a comet. As these tiny particles enter Earth’s atmosphere at high speeds,
                                    they burn up, creating bright flashes of light called meteors. Meteor showers are
                                    usually named after the constellation where they appear to originate, like the
                                    Perseids or Geminids. The best time to watch is during the peak hours after
                                    midnight, away from city lights, when the sky is dark and clear.</p>
                            </div>
                        </div>
                    </SwiperSlide>
                    <SwiperSlide>
                        <div className="event-intro">
                            <img src='/assets/jiguang_event.png' alt="aurora"/>
                            <div className="event-intro-item-content">
                                <h1>Aurora Introduction</h1>
                                <p>The aurora, also known as the northern or southern lights, is a natural light display
                                    in the sky, mainly seen in polar regions. It occurs when charged particles from the
                                    Sun collide with gases in Earth’s atmosphere, like oxygen and nitrogen. These
                                    collisions release energy, creating beautiful waves of green, pink, red, and purple
                                    lights that dance across the sky. Auroras are best viewed in dark, clear skies near
                                    the Arctic or Antarctic circles. The northern lights are called aurora borealis,
                                    while the southern lights are known as aurora australis.
                                </p>
                            </div>
                        </div>
                    </SwiperSlide>
                    <SwiperSlide>
                        <div className="event-intro">
                            <img src='/assets/solar_eclipse.png' alt="solar"/>
                            <div className="event-intro-item-content">
                                <h1>Solar Eclipse Introduction</h1>
                                <p>A solar eclipse occurs when the Moon passes between the Earth and the Sun, blocking
                                    all or part of the Sun's light. There are three main types: total, partial, and
                                    annular. In a total solar eclipse, the Moon completely covers the Sun, creating a
                                    brief period of darkness during the day. A partial eclipse covers only part of the
                                    Sun, while an annular eclipse leaves a bright ring of sunlight around the Moon.
                                    Solar eclipses are rare in the same location and require special eye protection to
                                    view safely. They offer a stunning, unforgettable sky phenomenon.
                                </p>
                            </div>
                        </div>
                    </SwiperSlide>
                    <SwiperSlide>
                        <div className="event-intro">
                            <img src='/assets/lunar_eclipse.png' alt="lunar"/>
                            <div className="event-intro-item-content">
                                <h1>Lunar Eclipse Introduction</h1>
                                <p>A lunar eclipse happens when the Earth comes between the Sun and the Moon, casting a
                                    shadow on the Moon. This occurs only during a full moon. There are three types:
                                    total, partial, and penumbral. In a total lunar eclipse, the entire Moon enters
                                    Earth's shadow and often turns a reddish color, known as a "blood moon." A partial
                                    eclipse covers only part of the Moon, while a penumbral eclipse causes a subtle
                                    shadow, making the Moon slightly dimmer. Unlike solar eclipses, lunar eclipses are
                                    safe to watch with the naked eye and can be seen from anywhere on Earth where the
                                    Moon is visible.
                                </p>
                            </div>
                        </div>
                    </SwiperSlide>
                    <div className="autoplay-progress" slot="container-end">
                        <svg viewBox="0 0 48 48" ref={progressCircle}>
                            <circle cx="24" cy="24" r="20"></circle>
                        </svg>
                        <span ref={progressContent}></span>
                    </div>
                </Swiper>
            </section>
            <hr/>
            <section>
                <div className="event_list-div">
                    <table className="event-list-tbl">
                        <thead>
                        <tr>
                            <th className='start-date'>Start Date</th>
                            <th className='event-img'>Event Image</th>
                            <th className="event-type">Event Type</th>
                            <th className='event-des'>Event Description</th>
                            <th className='event-des'>Recommended Sites</th>
                        </tr>
                        </thead>
                        <tbody>
                        {events.map((evt, rowIndex) => (
                            <tr key={rowIndex}>
                                <td className='start-date'>{evt.startDate.split('T')[0]}</td>
                                <td className='event-img'>
                                    <img src={`/assets/${evt.type.toLowerCase()}_logo.png`} alt={evt.type} />
                                </td>
                                <td className="event-type">{evt.type}</td>
                                <td className='event-des'>{evt.description}</td>
                                <td className='reco-sites'>
                                    <a href={evt.type === "METEOR"
                                        ? `/meteor_shower?event_id=${evt.eventId}`
                                        : "/eclipse"}>Recommended Sites</a>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </section>
        </div>
    );
}

export default Home;
