package com.example.backend.repositories;

import com.example.backend.models.FoodPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodPackageRepository extends JpaRepository<FoodPackage, Long> {

    /**
     * 根据餐厅或美食ID查找所有套餐
     *
     * @param foodId 餐厅或美食的唯一标识ID
     * @return 与指定餐厅或美食相关的所有套餐列表
     */
    List<FoodPackage> findByFood_FoodId(Long foodId);

    /**
     * 根据套餐ID查找指定套餐
     *
     * @param packageId 套餐的唯一标识ID
     * @return 指定的套餐对象
     */
    FoodPackage findByPackageId(Long packageId);

    /**
     * 删除指定ID的套餐
     *
     * @param packageId 套餐的唯一标识ID
     */
    void deleteByPackageId(Long packageId);

    /**
     * 检查套餐是否存在
     *
     * @param packageId 套餐的唯一标识ID
     * @return 如果套餐存在则返回true，否则返回false
     */
    boolean existsByPackageId(Long packageId);
}
