# 1.数据库创建备份用户
create user dumper@'%';
grant select on tempdb.* to dumper@'127.0.0.1';
grant show view on tempdb.* to dumper@'127.0.0.1';
grant lock tables on tempdb.* to dumper@'127.0.0.1';
grant trigger on tempdb.* to dumper@'127.0.0.1';

# 2.shell 脚本Start-----------------
#!/bin/sh
# Database info
DB_USER="dumper"
DB_PASS="123456"
DB_HOST="localhost"
# Database array
DB_NAME=("wisep")
# Others vars
BIN_DIR="/www/server/mysql/bin"      #the mysql bin path
BCK_DIR="/root/DatabaseBack"  #the backup file directory
DATE=`date +%Y-%m-%d-%H:%m:%S`
# create file
# mkdir $BCK_DIR/$DATE
# TODO
# /usr/bin/mysqldump --opt -ubatsing -pbatsingpw -hlocalhost timepusher > /mnt/mysqlBackup/db_`date +%F`.sql
for var in ${DB_NAME[@]};
do
  $BIN_DIR/mysqldump --opt --single-transaction --master-data=2 -u$DB_USER -p$DB_PASS -h$DB_HOST $DB_NAME > $BCK_DIR/$DATE-$var.sql
done
# shell 脚本End-----------------

# 参数说明：
# --master-data[=#]
# 在备份导出的文件里追加二进制binlog文件的位置和名称
# 如果值等于1，就会添加一个CHANGE MASTER语句
# 如果值等于2，就会在CHAGE MASTER语句前添加注释（不起作用了呗~）
# 这个参数会--lock-all-tables锁表，除非你指定了--single-transaction
# 这种情况下，锁表只会在dump开始的时候持续一小段时间,照理说 在dump的时候，任何动作都会影响到binlog文件 dump结束之后，选项会自动关闭锁表功能
# --single-transaction

# 以事务的形式执行

# 授予，定时任务备份即可