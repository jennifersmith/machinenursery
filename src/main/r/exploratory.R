# Exploratory Data Analysis
# What questions can we ask of the data to get a rough feel for it?
setwd("~/code/machine-learning/machinenursery")

titanic <- read.csv("./titanic-data/train.csv")

# Can you obviously see that one socio economic class died more?
plot(titanic$survived, titanic$pclass, pch=19, col="blue", cex=0.5)
smoothScatter(titanic$survived, titanic$pclass)

# You can roughly see that more people in the lower socio economic class (3) died
# than in the other classes but it doesn't look that significant

# How many of each socio economic class were on the ship?
# How many of each socio economic class survived? 
table(titanic$pclass)

survived <- function(class) {
  peopleInThatClass <- titanic[titanic$pclass == class,]
  survived <- peopleInThatClass[peopleInThatClass$survived == 1,]  
  nrow(survived)
}

passengers <- function(class) {
  peopleInThatClass <- titanic[titanic$pclass == class,]
  nrow(peopleInThatClass)
}

survivalSummary <- function() {
  summary<-NULL
  for(class in classes) {
    numberSurvived <- survived(class)
    total <- passengers(class)
    percentageSurvived <- numberSurvived / total
    rbind(summary,data.frame(class=class, survived=numberSurvived, total=total, percentSurvived=percentageSurvived))->summary
  }
  
  summary[with(summary, order(class)),]  
}

classes <- unique(titanic$pclass)



