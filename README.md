# DBay
Sample EBay-like backend.
Run up against time constraints... ok slightly over... fine, I geeked a little and spent way more time on this than I should have, hence the lack of unit tests or javadocs.
Operational over RESTful interface.

Sample test script:

http://localhost:8080/addUser?lastName=User&firstName=First&email=first@user.com&amount=100.0&currency=USD

http://localhost:8080/addUser?lastName=User&firstName=Second&email=second@user.com&amount=100.0&currency=USD

http://localhost:8080/addUser?lastName=User&firstName=Third&email=third@user.com&amount=100.0&currency=USD

http://localhost:8080/addNewAuction?ownerEmail=first@user.com&itemName=Vegas

http://localhost:8080/placeBid?ownerEmail=first@user.com&itemName=Vegas&amount=10.0&currency=USD&bidderEmail=second@user.com

http://localhost:8080/placeBid?ownerEmail=first@user.com&itemName=Vegas&amount=9.0&currency=USD&bidderEmail=third@user.com

http://localhost:8080/placeBid?ownerEmail=first@user.com&itemName=Vegas&amount=12.9&currency=CAD&bidderEmail=third@user.com

http://localhost:8080/placeBid?ownerEmail=first@user.com&itemName=Vegas&amount=11.0&currency=USD&bidderEmail=third@user.com

http://localhost:8080/placeBid?ownerEmail=first@user.com&itemName=Vegas&amount=11.0&currency=USD&bidderEmail=third@user.com

http://localhost:8080/endAuction?ownerEmail=first@user.com&itemName=Vegas

http://localhost:8080/placeBid?ownerEmail=first@user.com&itemName=Vegas&amount=11.0&currency=USD&bidderEmail=third@user.com
