require("RPostgreSQL")

# create a connection
# save the password that we can "hide" it as best as we can by collapsing it
pw <- {
  "ev093qer"
}

# loads the PostgreSQL driver
drv <- dbDriver("PostgreSQL")
# creates a connection to the postgres database
# note that "con" will be used later in each connection to the database
con <- dbConnect(drv, dbname = "postgres",
                 host = "132.147.88.190", port = 5433,
                 user = "ecoadm", password = pw)

### query for table 
library(DAAG)
library(boot)
library(MASS)
library(e1071)
df_postgres.train <- dbGetQuery(con, "select hourstamp,avg(conflow1) as conflow1 ,avg(conflow2) as conflow2 ,avg(conin1) as conin1, avg(conin2) as conin2, avg(conout1) as conout1 , avg(conout2) as conout2,avg(evaflow1) as evaflow1, avg(evaflow2) as evaflow2, avg(evain1) as evain1, avg(evain2) as evain2, avg(evaout1) as evaout1, avg(evaout2) as evaout2, avg(temp) as temp, avg(now) as rain,(max(consumptiontotal)-min(consumptiontotal)) as onehrconsump,avg(powertotal) as power from \"chiller_cbpt2\" where (hourstamp - '#combfinalts#') <= interval '0 seconds' and ('#combfinalts#'-hourstamp) >= interval '30 days' group by hourstamp")
df_postgres.test <- dbGetQuery(con, "select hourstamp,avg(conflow1) as conflow1 ,avg(conflow2) as conflow2 ,avg(conin1) as conin1, avg(conin2) as conin2, avg(conout1) as conout1 , avg(conout2) as conout2,avg(evaflow1) as evaflow1, avg(evaflow2) as evaflow2, avg(evain1) as evain1, avg(evain2) as evain2, avg(evaout1) as evaout1, avg(evaout2) as evaout2, avg(temp) as temp, avg(now) as rain,(max(consumptiontotal)-min(consumptiontotal)) as onehrconsump,avg(powertotal) as power from \"chiller_cbpt2\" where (hourstamp - '#combfinalts#') >= interval '0 seconds' group by hourstamp")

if(nrow(df_postgres.test)<=0){stop("no data for prediction task. end the program...")}

frml <- power~ conflow1+conflow2+conin1+conin2+conout1+conout2+evaflow1+evaflow2+evain1+evain2+evaout1+evaout2+temp+rain

#SVM
#fit_svm <- svm(frml, data = df_postgres.train, kernel = "polynomial",degree=5,gamma =0.5)
#pred_svm <- predict(fit_svm, newdata=df_postgres.test)
#regression
frml4=power~ conflow1 + I(conflow1^2) +I(conflow1^3)+I(conflow1^4)+conflow2 + I(conflow2^2) +I(conflow2^3)+I(conflow2^4)+conin1+I(conin1^2)+I(conin1^3)+I(conin1^4)+conin2+I(conin2^2)+I(conin2^3)+I(conin2^4)+conout1+I(conout1^2)+I(conout1^3)+I(conout1^4)+conout2+I(conout2^2)+I(conout2^3)+I(conout2^4)+evaflow1+I(evaflow1^2) +I(evaflow1^3)+I(evaflow1^4)+evaflow2 + I(evaflow2^2) +I(evaflow2^3)+I(evaflow2^4)+evain1+I(evain1^2)+I(evain1^3)+I(evain1^4)+evain2+I(evain2^2)+I(evain2^3)+I(evain2^4)+evaout1+I(evaout1^2)+I(evaout1^3)+I(evaout1^4)+evaout2+I(evaout2^2)+I(evaout2^3)+I(evaout2^4)
fit4_sp=lm(frml4,data=df_postgres.train)
fit4_sp_step=stepAIC(fit4_sp,direction="both")
pred_svm <- predict(fit4_sp_step, newdata=df_postgres.test)

actualvspred_svm=cbind(df_postgres.test$power,pred_svm,abs(df_postgres.test$power-pred_svm))
#plot(actualvspred_svm[,1],actualvspred_svm[,2],main="predicted against actual for svm polynomial 0.5",xlab="actual",ylab="predicted",asp=1, pty='s')
#abline(0, 1)
mse_svm <- sqrt(sum((actualvspred_svm[,3])^2)/nrow(df_postgres.test))

#write back predictions need to remove fit for svm
predall <- predict(fit4_sp_step,newdata=df_postgres.test,se=TRUE)
results <- cbind(df_postgres.test,predall$fit)
results$`predall$fit` <- abs(results$`predall$fit`)
results <- results[order(results$hourstamp),]


time = c(1:nrow(results))
#plot(time,results$power,col="blue",main="predicted against actual polynomial regression ",xlab="time",ylab="power",type='l')
#lines(time,results$`predall$fit`,col="red")
show(mse_svm)
summary(fit4_sp_step)
dbWriteTable(con, "test", value=results, overwrite=FALSE, append=TRUE, row.names=FALSE)
dbDisconnect(con)