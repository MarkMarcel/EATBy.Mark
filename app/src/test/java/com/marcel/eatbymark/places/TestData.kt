package com.marcel.eatbymark.places

import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.models.PlaceDish
import com.marcel.eatbymark.places.models.PlaceImage
import com.marcel.eatbymark.places.models.PlacePrice


val placesTestData = listOf(
    Place(
        id = "5ae6013cf78b5a000bb64022",
        name = "McDonald's Helsinki Kamppi",
        shortDescription = "I'm lovin' it.",
        isOnline = true,
        isFavourite = false,
        telemetryId = "6829eb11ca73b227dd015b65",
        image = PlaceImage(
            blurHash = "jfSbKEjtXKXtXlPdgOhk;;OYgPhC",
            url = "https://imageproxy.wolt.com/assets/6735be5986f45b72713e2129"
        ),
        dishes = listOf(
            PlaceDish(
                id = "67bd4785a6cff00446855937",
                name = "Tuplajuustoateria",
                image = PlaceImage(
                    blurHash = "U:QI;=xZ?wEgx]kCROjFtlbbV?nhs;afWBkA",
                    url = "https://wolt-menu-images-cdn.wolt.com/menu-images/5e835543815f65fa2b18072c/b892e2da-f003-11ed-923a-06acece2da46_fd_fi_8013_55.png"
                ),
                price = PlacePrice(currency = "EUR", value = 1035)
            ),
            PlaceDish(
                id = "67bd4785a6cff00446855933",
                name = "Big Mac™ -ateria",
                image = PlaceImage(
                    blurHash = "U;P?UFxZ?wIp%MofM{WBx^bbRPn\$bbWCn%od",
                    url = "https://wolt-menu-images-cdn.wolt.com/menu-images/5e835543815f65fa2b18072c/b88fa6e2-f003-11ed-923a-06acece2da46_fd_fi_8010_55.png"
                ),
                price = PlacePrice(currency = "EUR", value = 1230)
            )
            // ... add other PlaceDish objects similarly
        )
    ),
    Place(
        id = "5efdd98114d8366811bc58e1",
        name = "Burger King Kamppi",
        shortDescription = "Liekillä grillatut hampurilaiset",
        isOnline = true,
        isFavourite = false,
        telemetryId = "6829eb11ca73b227dd015b67",
        image = PlaceImage(
            blurHash = "jaTsCPXtTLrd;;8yPbOXX;XKh4gP",
            url = "https://imageproxy.wolt.com/assets/67332ff2a7cc62434f9d8b2f"
        ),
        dishes = listOf(
            PlaceDish(
                id = "676939e631840d1d21139955",
                name = "Steakhouse -iso ateria",
                image = PlaceImage(
                    blurHash = "UqQ9TJ%M?vR*?wM{Rjxux^WAROoMROobjGW?",
                    url = "https://wolt-menu-images-cdn.wolt.com/menu-images/619b88aea30d327a15cb9a73/f0705e80-3138-11f0-b6ed-6e3a01622fbb.jpeg"
                ),
                price = PlacePrice(currency = "EUR", value = 1645)
            )
        )
    ),
    Place(
        id = "6348098a9157ab2b10bdaf65",
        name = "Bastard Burgers Mikonkatu",
        shortDescription = "Like a Bastard™",
        isOnline = true,
        isFavourite = false,
        telemetryId = "6829eb11ca73b227dd015b68",
        image = PlaceImage(
            blurHash = "j4rlRf:QXK01P;0hTKlB9J4zXs;I",
            url = "https://imageproxy.wolt.com/assets/673206ee63ae2357e7f7b394"
        ),
        dishes = listOf(
            PlaceDish(
                id = "67dbda2656a6f0831337ecb1",
                name = "New York Meal",
                image = PlaceImage(
                    blurHash = "UGKS|EMx~A^c]}M{ENxtRqIqRk9w0Nae%0Ia",
                    url = "https://wolt-menu-images-cdn.wolt.com/menu-images/634e6c37a5a5e8824e130685/de658610-105f-11f0-be79-7a1695a30c90.jpeg"
                ),
                price = PlacePrice(currency = "EUR", value = 1740)
            )
        )
    )
)
