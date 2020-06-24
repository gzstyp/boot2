<#ftl encoding="utf-8"/>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${nameSpace}">

    <sql id="sql_column">
    <#list listData as ld>
    <#if ld_has_next>
        <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name},</if>
    <#else>
        <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name}</if>
    </#if>
    </#list>
    </sql>

    <!-- 添加数据,主键是否是自增,如果自增则HashMap的参数不要put("主键",值),然后通过map参数的get("主键")获取自增值 -->
    <update id="add" parameterType="HashMap" useGeneratedKeys="true" keyProperty="${keyId}">
        INSERT INTO ${tableName} (
        <trim suffixOverrides=",">
		<#list listData as ld>
		<#if ld_has_next>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name},</if>
        <#else>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name}</if>
        </#if>
        </#list>
        </trim>
        ) VALUES (
        <trim suffixOverrides=",">
        <#list listData as ld>
		<#if ld_has_next>
			<if test="${ld.column_name} != null and ${ld.column_name} != ''">${r"#{"}${ld.column_name}${r"}"},</if>
        <#else>
			<if test="${ld.column_name} != null and ${ld.column_name} != ''">${r"#{"}${ld.column_name}${r"}"}</if>
        </#if>
        </#list>
        </trim>
        )
    </update>

    <!-- 基于主键${keyId}的存在就更新,否则新增操作 -->
    <update id="updateOrEdit" parameterType="HashMap" useGeneratedKeys="true" keyProperty="${keyId}">
        INSERT IGNORE INTO ${tableName} (
        <trim suffixOverrides=",">
        <#list listData as ld>
        <#if ld_has_next>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name},</if>
        <#else>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${ld.column_name}</if>
        </#if>
        </#list>
        </trim>
        ) VALUES (
        <trim suffixOverrides=",">
        <#list listData as ld>
        <#if ld_has_next>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${r"#{"}${ld.column_name}${r"}"},</if>
        <#else>
            <if test="${ld.column_name} != null and ${ld.column_name} != ''">${r"#{"}${ld.column_name}${r"}"}</if>
        </#if>
        </#list>
        </trim>
        )
        ON DUPLICATE KEY UPDATE
        <trim suffixOverrides=",">
            <#list listEdit as le>
                <#if le_has_next>
            <if test="${le.column_name} != null and ${le.column_name} != ''">${le.column_name} = ${r"#{"}${le.column_name}${r"}"},</if>
                <#else>
            <if test="${le.column_name} != null and ${le.column_name} != ''">${le.column_name} = ${r"#{"}${le.column_name}${r"}"}</if>
                </#if>
            </#list>
        </trim>
    </update>

    <!-- 基于主键${keyId}的批量插入还是批量更新操作 VALUES (字段) VALUES内是数据库的字段，而不是实体的字段或map的key -->
    <update id="updateEditBatch" parameterType="ArrayList">
        INSERT IGNORE INTO ${tableName} (
        <foreach collection="list" item="item" index="index">
            <trim suffixOverrides=",">
                <if test="index==0">
                <#list listData as ld>
                    <if test="${r"item."}${ld.column_name} != null and ${r"item."}${ld.column_name} != ''">${ld.column_name},</if>
                </#list>
                </if>
            </trim>
        </foreach>
        ) VALUES
        <foreach collection="list" item="item" index="index" separator="," >
            (
            <trim suffixOverrides=",">
               <#list listData as ld>
                <#if ld_has_next>
                <if test="${r"item."}${ld.column_name} != null and ${r"item."}${ld.column_name} != ''">${r"#{item."}${ld.column_name}${r"}"},</if>
                <#else>
                <if test="${r"item."}${ld.column_name} != null and ${r"item."}${ld.column_name} != ''">${r"#{item."}${ld.column_name}${r"}"}</if>
                </#if>
               </#list>
            </trim>
            )
        </foreach>
        ON DUPLICATE KEY UPDATE
        <foreach collection="list" item="item" index="index">
            <trim suffixOverrides=",">
                <if test="index==0">
            <#list listData as ld>
                <if test="${r"item."}${ld.column_name} != null and ${r"item."}${ld.column_name} != ''">${ld.column_name} = values(${ld.column_name}),</if>
            </#list>
                </if>
            </trim>
        </foreach>
    </update>
	
	<!-- 编辑数据 -->
    <update id="edit" parameterType="HashMap">
        UPDATE ${tableName}
        <trim prefix="SET" suffixOverrides=",">
        <#list listEdit as le>
        <#if "${keyId}" != "${le.column_name}">
        <#if le_has_next>
            <if test="${le.column_name} != null and ${le.column_name} != ''">${le.column_name} = ${r"#{"}${le.column_name}${r"}"},</if>
        <#else>
            <if test="${le.column_name} != null and ${le.column_name} != ''">${le.column_name} = ${r"#{"}${le.column_name}${r"}"}</if>
        </#if>
        </#if>
        </#list>
        </trim>
        WHERE ${keyId} = ${r"#{"}${keyId}${r"}"} LIMIT 1
    </update>
	
	<!-- 行删除 -->
	<delete id="delById" parameterType="String">
		DELETE FROM ${tableName} WHERE ${keyId} = ${r"#{"}${keyId}${r"}"}
	</delete>
	
	<!-- 删除|批量删除-->
	<delete id="delIds">
		DELETE FROM ${tableName} WHERE 
			${keyId} IN
		<foreach item="item" index="index" collection="list" open="(" separator="," close=")">
                 ${r"#{item}"}
		</foreach>
	</delete>

    <!-- 批量添加|插入,所有传List的Map的value必须同时为空或同时不为空,否则会报错-->
    <update id="batchAdd" parameterType="ArrayList">
        INSERT IGNORE INTO ${tableName} (
            <foreach collection="list" item="item" index="index">
                <trim suffixOverrides=",">
                    <if test="index==0">
                    <#list listData as ld>
                        <if test="${r"item."}${ld.column_name} != null and ${r"item."}${ld.column_name} != ''">${ld.column_name},</if>
                    </#list>
                    </if>
                </trim>
            </foreach>
        ) VALUES
        <foreach collection="list" item="item" index="index" separator="," >
            (
            <trim suffixOverrides=",">
               <#list listData as ld>
                <#if ld_has_next>
                <if test="${r"item."}${ld.column_name} != null and ${r"item."}${ld.column_name} != ''">${r"#{item."}${ld.column_name}${r"}"},</if>
                <#else>
                <if test="${r"item."}${ld.column_name} != null and ${r"item."}${ld.column_name} != ''">${r"#{item."}${ld.column_name}${r"}"}</if>
                </#if>
               </#list>
            </trim>
            )
        </foreach>
    </update>
	
	<!-- listData条件查询 -->
    <sql id="sql_where_listData">
        <trim prefix="WHERE" prefixOverrides="AND">
        <#list listEdit as le>
        <#if "${keyId}" != "${le.column_name}">
            <if test="${le.column_name} != null and ${le.column_name} != ''">
                AND ${le.column_name} LIKE CONCAT('%',${r"#{"}${le.column_name}${r"}"},'%')
            </if>
        </#if>
        </#list>
        </trim>
    </sql>
	
	<!-- 列表数据 -->
	<select id="listData" parameterType="HashMap" resultType="HashMap">
		SELECT
		<#list listData as ld>
		<#if ld_has_next>
			${ld.column_name},
		<#else>
			${ld.column_name}
		</#if>
		</#list>
		FROM ${tableName}
		<include refid="sql_where_listData"/>
		<choose>
    	<when test="order != null and sort != null">
    		ORDER BY ${r'${sort}'} ${r'${order}'}
    	</when>
    	<otherwise>
        </otherwise>
        </choose>
        <if test="section != null and pageSize != null ">
        	LIMIT ${r"#{section}"},${r"#{pageSize}"}
		</if>
	</select>
	
	<!-- 总条数总记录数 -->
	<select id="listTotal" parameterType="HashMap" resultType="Integer">
		SELECT COUNT(${keyId}) TOTAL FROM ${tableName}
		<include refid="sql_where_listData"/>
	</select>
	
	<!-- 根据id获取全字段数据 -->
    <select id="queryById" parameterType="String" resultType="HashMap">
        SELECT
        <#list listEdit as le>
        <#if "${keyId}" != "${le.column_name}">
        <#if le_has_next>
            ${le.column_name},
        <#else>
            ${le.column_name}
        </#if>
        </#if>
        </#list>
        FROM ${tableName} WHERE ${keyId} = ${r"#{"}${keyId}${r"}"} LIMIT 1
    </select>

	<!-- 查询全部数据 -->
	<select id="listAll" parameterType="HashMap" resultType="HashMap">
		SELECT
		<#list listData as ld>
		<#if ld_has_next>
			${ld.column_name},
		<#else>
			${ld.column_name}
		</#if>
		</#list>
		FROM ${tableName}
		<trim prefix="WHERE" prefixOverrides="AND">
		<#list listData as ld>
		<if test="${ld.column_name} != null and ${ld.column_name} != ''">
			<![CDATA[ AND ${ld.column_name} LIKE CONCAT('%',${r"#{"}${ld.column_name}${r"}"},'%') ]]>
		</if>
		</#list>
		</trim>
	</select>
</mapper>