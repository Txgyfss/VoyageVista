package com.example.backend.repositories;

import com.example.backend.models.Food;
import com.example.backend.models.FoodPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    // 根据餐厅或美食名称查找
    Food findByName(String name);

    // 根据城市查找餐厅或美食
    List<Food> findByCity(String city);

    // 查找特定评分以上的餐厅或美食
    List<Food> findByRatingGreaterThanEqual(BigDecimal rating);

    // 使用自定义查询，通过价格范围查找餐厅或美食
    @Query("SELECT f FROM Food f JOIN f.packages p WHERE p.price BETWEEN ?1 AND ?2")
    List<Food> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    // 根据餐厅或美食ID查找所有套餐
    List<FoodPackage> findPackagesByFoodId(Integer foodId);

    // 根据餐厅或美食ID更新信息
    @Modifying
    @Transactional
    @Query("UPDATE Food f SET f.name = :name, f.city = :city, f.rating = :rating WHERE f.foodId = :id")
    void updateFoodById(Integer id, String name, String city, BigDecimal rating);

    // 删除餐厅或美食
    void deleteByName(String name);

    @Query("SELECT f FROM Food f WHERE (:name IS NULL OR f.name LIKE %:name%)" +
            "AND (:city IS NULL OR f.city LIKE %:city%)" +
            "AND (:type IS NULL OR f.type LIKE %:type%)")
    List<Food> findByQueryParams(@Param("name") String name, @Param("city") String city, @Param("type") String type);

    // 更新美食评分
    @Modifying
    @Transactional
    @Query("UPDATE Food a SET a.rating = ?2 WHERE a.foodId = ?1")
    void updateRatingById(Integer id, BigDecimal rating);
}
