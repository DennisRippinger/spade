library("maps")
library("mapproj")

locations <- read.delim("~/Desktop/locations.csv")
coord <- mapproject(locations$Longitude, locations$Latitude, proj="mercator", orientation=c(90,0,255))

map("state", interior = FALSE, projection="mercator", orientation=c(90,0,255))
map("state", boundary = FALSE, col="gray", projection="mercator", orientation=c(90,0,255), add = TRUE)
points(coord, pch=20, cex=0.8, col=map.colors)
