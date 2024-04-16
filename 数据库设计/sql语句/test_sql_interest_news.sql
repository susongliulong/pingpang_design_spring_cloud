# 更新话题热点数据
UPDATE interest
SET hots=(
	SELECT COUNT(*)
	FROM basic_information b
	WHERE b.`category_id`=interest.`id`
)
WHERE interest.`id`>=28;

# 查询每一位用户发表的文章信息的数量
SELECT u.`name`,COUNT(b.`id`) total
FROM USER u, basic_information b
WHERE u.`id`=b.`author_id`
GROUP BY u.`name`
ORDER BY total DESC;

# 更新数据
DELETE FROM USER 
WHERE id>6;
DELETE FROM basic_information
WHERE id>1;

# 更新basic_information中的封面信息
UPDATE  basic_information
SET cover_image='https://img.pingpangwang.com/uc_server/data/avatar/000/11/36/84_avatar_middle.jpg'
WHERE id>1;


UPDATE basic_information
SET main_content='首个比赛日，国乒取得9胜2平战绩，其中八场4-0轻松获胜，男单樊振东、梁靖崑、马龙、林诗栋，女单孙颖莎、王曼昱、陈梦、陈幸同，王艺迪虽然丢掉一局，但考虑到她的对手是杜凯琹，这个比分也很正常。
内容来源：乒乓网
原文地址：https://www.pingpangwang.com/thread-539758-1-1.html'
WHERE id >1;

SELECT * FROM news
WHERE news_id=1780061631094210600;


# 根据basic_information表更新news表中的数据
