package ar.edu.utn.frba.placesify.api

// https://location.foursquare.com/developer/reference/place-search
// API KEY : fsq3ten8up5hqIpNtmH/nBtowl9DjkZMnueqEh7ZRl9UXAw=
data class FoursquareIcons(
    var prefix: String,
    var suffix: String
)
data class FoursquareChains(
    var id: Int,
    var name: String
)
data class FoursquareCategories(
    var icon: FoursquareIcons,
    var id: Int,
    var name: String
)
data class FoursquarePlace(
    // TODO Definir data class
    var categories: List<FoursquareCategories>,
    var chains: List<FoursquareChains>,
    var distance: Int,
    var fsq_id: String,
    var geocodes: String,
    var link: String,
    var location: String,
    var name: String,
    var related_places: String,
    var timezone: String,

)
data class FoursquarePlaceResponse(
    // TODO Definir data class de la respuesta de la API de Foursquare
    var name: String,
    var job: String
)



/*
{
    "context": {
        "geo_bounds": {
            "circle": {
                "center": {
                    "latitude": 39.0469,
                    "longitude": -77.4903
                },
                "radius": 22000
            }
        }
    },
    "results": [
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/mediterranean_",
                        "suffix": ".png"
                    },
                    "id": 13302,
                    "name": "Mediterranean Restaurant"
                }
            ],
            "chains": [
                {
                    "id": "ade3eabc-5994-4a8e-b5b1-9b40d4e6c1e4",
                    "name": "Cava Grill"
                }
            ],
            "distance": 2326,
            "fsq_id": "55d485d2498ed80b1a40d08c",
            "geocodes": {
                "main": {
                    "latitude": 39.067475,
                    "longitude": -77.485264
                },
                "roof": {
                    "latitude": 39.067475,
                    "longitude": -77.485264
                }
            },
            "link": "/v3/places/55d485d2498ed80b1a40d08c",
            "location": {
                "address": "19825 Belmont Chase Dr",
                "address_extended": "Ste 100",
                "census_block": "511076110122000",
                "country": "US",
                "cross_street": "",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "19825 Belmont Chase Dr, Ashburn, VA 20147",
                "locality": "Ashburn",
                "postcode": "20147",
                "region": "VA"
            },
            "name": "Cava",
            "related_places": {
                "parent": {
                    "fsq_id": "59a35ef9625a6632a8780bfa",
                    "name": "Chase Bank"
                }
            },
            "timezone": "America/New_York"
        },
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/shops/food_grocery_",
                        "suffix": ".png"
                    },
                    "id": 17069,
                    "name": "Grocery Store"
                }
            ],
            "chains": [
                {
                    "id": "c356152c-f98c-4c43-8a1e-1d16cc9ea1c8",
                    "name": "Whole Foods"
                }
            ],
            "distance": 2452,
            "fsq_id": "55aca43c498e17c4894188e5",
            "geocodes": {
                "main": {
                    "latitude": 39.068541,
                    "longitude": -77.484242
                },
                "roof": {
                    "latitude": 39.068541,
                    "longitude": -77.484242
                }
            },
            "link": "/v3/places/55aca43c498e17c4894188e5",
            "location": {
                "address": "19800 Belmont Chase Dr",
                "address_extended": "Ste 160",
                "census_block": "511076110122000",
                "country": "US",
                "cross_street": "Claiborne Pkwy",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "19800 Belmont Chase Dr (Claiborne Pkwy), Ashburn, VA 20147",
                "locality": "Ashburn",
                "postcode": "20147",
                "region": "VA"
            },
            "name": "Whole Foods",
            "related_places": {
                "children": [],
                "parent": {
                    "fsq_id": "59a35ef9625a6632a8780bfa",
                    "name": "Chase Bank"
                }
            },
            "timezone": "America/New_York"
        },
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/newamerican_",
                        "suffix": ".png"
                    },
                    "id": 13314,
                    "name": "New American Restaurant"
                }
            ],
            "chains": [
                {
                    "id": "82cf5d0a-13ce-4473-8d4a-4c9cb82b6b17",
                    "name": "Cooper's Hawk Winery and Restaurant"
                }
            ],
            "distance": 2242,
            "fsq_id": "56b54c9a498e420242d875f0",
            "geocodes": {
                "main": {
                    "latitude": 39.066356,
                    "longitude": -77.484462
                },
                "roof": {
                    "latitude": 39.066356,
                    "longitude": -77.484462
                }
            },
            "link": "/v3/places/56b54c9a498e420242d875f0",
            "location": {
                "address": "19800-19890 Belmont Chase Dr",
                "address_extended": "Street Level",
                "census_block": "511076110122002",
                "country": "US",
                "cross_street": "",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "19800-19890 Belmont Chase Dr, Ashburn, VA 20147",
                "locality": "Ashburn",
                "postcode": "20147",
                "region": "VA"
            },
            "name": "Cooper's Hawk Winery & Restaurant- Ashburn",
            "related_places": {
                "parent": {
                    "fsq_id": "59a35ef9625a6632a8780bfa",
                    "name": "Chase Bank"
                }
            },
            "timezone": "America/New_York"
        },
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/arts_entertainment/movietheater_",
                        "suffix": ".png"
                    },
                    "id": 10024,
                    "name": "Movie Theater"
                }
            ],
            "chains": [
                {
                    "id": "2dedf34b-34db-49aa-b458-1f9b5628b5c7",
                    "name": "Alamo Drafthouse Cinemas"
                }
            ],
            "distance": 3142,
            "fsq_id": "513514fc39501a298d00de1c",
            "geocodes": {
                "main": {
                    "latitude": 39.052064,
                    "longitude": -77.455031
                },
                "roof": {
                    "latitude": 39.052064,
                    "longitude": -77.455031
                }
            },
            "link": "/v3/places/513514fc39501a298d00de1c",
            "location": {
                "address": "20575 Easthampton Plz",
                "census_block": "511076110152039",
                "country": "US",
                "cross_street": "Russell Branch Pkwy",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "20575 Easthampton Plz (Russell Branch Pkwy), Ashburn, VA 20147",
                "locality": "Ashburn",
                "postcode": "20147",
                "region": "VA"
            },
            "name": "Alamo Drafthouse One Loudoun",
            "related_places": {
                "children": [
                    {
                        "fsq_id": "517bf408e4b00db680eef246",
                        "name": "Glass Half Full at Alamo Drafthouse Cinema"
                    }
                ],
                "parent": {
                    "fsq_id": "4f53d345e4b0b5893994e0eb",
                    "name": "One Loudoun"
                }
            },
            "timezone": "America/New_York"
        },
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/nightlife/cocktails_",
                        "suffix": ".png"
                    },
                    "id": 13009,
                    "name": "Cocktail Bar"
                },
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/thai_",
                        "suffix": ".png"
                    },
                    "id": 13352,
                    "name": "Thai Restaurant"
                }
            ],
            "chains": [],
            "distance": 3166,
            "fsq_id": "5585e12b498ee7320fe65d99",
            "geocodes": {
                "main": {
                    "latitude": 39.051476,
                    "longitude": -77.454204
                },
                "roof": {
                    "latitude": 39.051476,
                    "longitude": -77.454204
                }
            },
            "link": "/v3/places/5585e12b498ee7320fe65d99",
            "location": {
                "address": "20413 Exchange St",
                "census_block": "511076110152045",
                "country": "US",
                "cross_street": "",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "20413 Exchange St, Ashburn, VA 20147",
                "locality": "Ashburn",
                "postcode": "20147",
                "region": "VA"
            },
            "name": "Sense of Thai St",
            "related_places": {
                "parent": {
                    "fsq_id": "4f53d345e4b0b5893994e0eb",
                    "name": "One Loudoun"
                }
            },
            "timezone": "America/New_York"
        },
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/african_",
                        "suffix": ".png"
                    },
                    "id": 13067,
                    "name": "African Restaurant"
                },
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/portuguese_",
                        "suffix": ".png"
                    },
                    "id": 13325,
                    "name": "Portuguese Restaurant"
                }
            ],
            "chains": [],
            "distance": 3171,
            "fsq_id": "53a8b688498ef1dd73249fa5",
            "geocodes": {
                "main": {
                    "latitude": 39.052679,
                    "longitude": -77.454465
                },
                "roof": {
                    "latitude": 39.052679,
                    "longitude": -77.454465
                }
            },
            "link": "/v3/places/53a8b688498ef1dd73249fa5",
            "location": {
                "address": "20556 Easthampton Plz",
                "census_block": "511076110152042",
                "country": "US",
                "cross_street": "",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "20556 Easthampton Plz, Ashburn, VA 20147",
                "locality": "Ashburn",
                "postcode": "20147",
                "region": "VA"
            },
            "name": "Nando's PERi-PERi",
            "related_places": {
                "parent": {
                    "fsq_id": "4f53d345e4b0b5893994e0eb",
                    "name": "One Loudoun"
                }
            },
            "timezone": "America/New_York"
        },
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/burger_",
                        "suffix": ".png"
                    },
                    "id": 13031,
                    "name": "Burger Joint"
                },
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/newamerican_",
                        "suffix": ".png"
                    },
                    "id": 13314,
                    "name": "New American Restaurant"
                },
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/seafood_",
                        "suffix": ".png"
                    },
                    "id": 13338,
                    "name": "Seafood Restaurant"
                }
            ],
            "chains": [],
            "distance": 2805,
            "fsq_id": "4acfb708f964a5207dd520e3",
            "geocodes": {
                "main": {
                    "latitude": 39.032122,
                    "longitude": -77.51659
                },
                "roof": {
                    "latitude": 39.032122,
                    "longitude": -77.51659
                }
            },
            "link": "/v3/places/4acfb708f964a5207dd520e3",
            "location": {
                "address": "42920 Broadlands Blvd",
                "census_block": "511076110231000",
                "country": "US",
                "cross_street": "at Chickacoan Trail Dr",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "42920 Broadlands Blvd (at Chickacoan Trail Dr), Broadlands, VA 20148",
                "locality": "Broadlands",
                "postcode": "20148",
                "region": "VA"
            },
            "name": "Clyde's Willow Creek Farm",
            "related_places": {},
            "timezone": "America/New_York"
        },
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/winery_",
                        "suffix": ".png"
                    },
                    "id": 13025,
                    "name": "Wine Bar"
                },
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/default_",
                        "suffix": ".png"
                    },
                    "id": 13027,
                    "name": "Bistro"
                },
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/food/tapas_",
                        "suffix": ".png"
                    },
                    "id": 13347,
                    "name": "Tapas Restaurant"
                }
            ],
            "chains": [],
            "distance": 2861,
            "fsq_id": "4bde14c86198c9b677ed12ff",
            "geocodes": {
                "main": {
                    "latitude": 39.026598,
                    "longitude": -77.510513
                },
                "roof": {
                    "latitude": 39.026598,
                    "longitude": -77.510513
                }
            },
            "link": "/v3/places/4bde14c86198c9b677ed12ff",
            "location": {
                "address": "43135 Broadlands Center Plz",
                "address_extended": "Ste 121",
                "census_block": "511076110233000",
                "country": "US",
                "cross_street": "",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "43135 Broadlands Center Plz, Broadlands, VA 20148",
                "locality": "Broadlands",
                "postcode": "20148",
                "region": "VA"
            },
            "name": "Parallel Wine Bistro",
            "related_places": {},
            "timezone": "America/New_York"
        },
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/shops/food_grocery_",
                        "suffix": ".png"
                    },
                    "id": 17069,
                    "name": "Grocery Store"
                }
            ],
            "chains": [],
            "distance": 2852,
            "fsq_id": "5aa17993286fda7cb9522270",
            "geocodes": {
                "main": {
                    "latitude": 39.027606,
                    "longitude": -77.511388
                },
                "roof": {
                    "latitude": 39.027606,
                    "longitude": -77.511388
                }
            },
            "link": "/v3/places/5aa17993286fda7cb9522270",
            "location": {
                "address": "21285 Coopers Hawk Dr",
                "census_block": "511076110232000",
                "country": "US",
                "cross_street": "",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "21285 Coopers Hawk Dr, Broadlands, VA 20148",
                "locality": "Broadlands",
                "postcode": "20148",
                "region": "VA"
            },
            "name": "Lidl",
            "related_places": {},
            "timezone": "America/New_York"
        },
        {
            "categories": [
                {
                    "icon": {
                        "prefix": "https://ss3.4sqi.net/img/categories_v2/shops/mall_",
                        "suffix": ".png"
                    },
                    "id": 17114,
                    "name": "Shopping Mall"
                }
            ],
            "chains": [],
            "distance": 3171,
            "fsq_id": "4f53d345e4b0b5893994e0eb",
            "geocodes": {
                "main": {
                    "latitude": 39.051416,
                    "longitude": -77.454324
                },
                "roof": {
                    "latitude": 39.051416,
                    "longitude": -77.454324
                }
            },
            "link": "/v3/places/4f53d345e4b0b5893994e0eb",
            "location": {
                "address": "20626 Easthampton Plz",
                "census_block": "511076110152045",
                "country": "US",
                "cross_street": "",
                "dma": "Washington, Dc-Hagrstwn",
                "formatted_address": "20626 Easthampton Plz, Ashburn, VA 20147",
                "locality": "Ashburn",
                "postcode": "20147",
                "region": "VA"
            },
            "name": "One Loudoun",
            "related_places": {
                "children": [
                    {
                        "fsq_id": "59665bd28fb09e76fa73d259",
                        "name": "Sterling Restaurant Supply"
                    },
                    {
                        "fsq_id": "54fa260b498e95a553e88921",
                        "name": "Uncle Julio's"
                    },
                    {
                        "fsq_id": "53568eff498e863ed3a1465d",
                        "name": "Elevation Burger"
                    },
                    {
                        "fsq_id": "5523ff36498e319491314ae2",
                        "name": "Pind Indian Cuisine"
                    },
                    {
                        "fsq_id": "5c12919fcabcff002bbfdedf",
                        "name": "Slapfish"
                    },
                    {
                        "fsq_id": "53a8b688498ef1dd73249fa5",
                        "name": "Nando's PERi-PERi"
                    },
                    {
                        "fsq_id": "594386423ba76704b8f1c411",
                        "name": "Starbucks"
                    },
                    {
                        "fsq_id": "5ce1d2f7a92d98002c7eebf1",
                        "name": "Fantasticks"
                    },
                    {
                        "fsq_id": "589e769bfc73d453c22476d0",
                        "name": "Eddie Merlot's"
                    },
                    {
                        "fsq_id": "52dd85eb498ebca1e43bf4d1",
                        "name": "Exxon"
                    },
                    {
                        "fsq_id": "5249b1e411d202eaa1829905",
                        "name": "Bar Louie - One Loudoun"
                    },
                    {
                        "fsq_id": "5a15e3b7ea1e44656859129b",
                        "name": "Barnes & Noble"
                    },
                    {
                        "fsq_id": "56aa8ee2498efe2032cca48d",
                        "name": "Phenix Salon Suites"
                    },
                    {
                        "fsq_id": "513514fc39501a298d00de1c",
                        "name": "Alamo Drafthouse One Loudoun"
                    },
                    {
                        "fsq_id": "563cb1ea237c9b303252e8c6",
                        "name": "Matchbox Vintage Pizza Bistro"
                    },
                    {
                        "fsq_id": "544c1115498ea3736c1e5dd3",
                        "name": "The Barn at One Loudoun"
                    },
                    {
                        "fsq_id": "547a4426498ee2521eb960d7",
                        "name": "Okada Japanese Restaurant"
                    },
                    {
                        "fsq_id": "52af1fdb11d2abeac770856d",
                        "name": "Firehouse Subs"
                    },
                    {
                        "fsq_id": "533dd1a4498e97570c7fd2f9",
                        "name": "Zoe's Kitchen"
                    },
                    {
                        "fsq_id": "63de7fd4d70df80670ab858b",
                        "name": "Lululemon Athletica"
                    },
                    {
                        "fsq_id": "5585e12b498ee7320fe65d99",
                        "name": "Sense of Thai St"
                    }
                ]
            },
            "timezone": "America/New_York"
        }
    ]
}
*/
