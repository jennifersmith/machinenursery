# Exploratory Data Analysis
# What questions can we ask of the data to get a rough feel for it?
setwd("~/code/machine-learning/machinenursery")

titanic <- read.csv("./titanic-data/train.csv")

# Can you obviously see that one socio economic class died more?

plot(titanic$survived, titanic$pclass, pch=19, col="blue", cex=0.5)
smoothScatter(titanic$survived, titanic$pclass)