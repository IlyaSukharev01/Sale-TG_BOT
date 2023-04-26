package ru.smokebot.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.smokebot.dto.Package;
import ru.smokebot.dto.tables.SaleProducts;
import ru.smokebot.repository.jpa.SaleProductsInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShopModel
{
    @Autowired private SaleProductsInterface saleProductsInterface;


    public void addProduct(SaleProducts product)
    {
        saleProductsInterface.save(product);
    }


    /**
     *
     * @return
     * Функция, которая возвращает все
     * продукты из saleproducts по убыванию
     * общего количества продуктов (commonCount)
     */
    private List<SaleProducts> getAllProductsDesc()
    {
        return saleProductsInterface.findByOrderByCommonCountDesc();
    }


    /**
     *
     * @return
     * Возвращает список всех текущих товаров,
     * общее количество которых больше 0.
     * Товары берутся из saleproducts.
     */
    private List<SaleProducts> getAllActiveProducts()
    {
        return saleProductsInterface.findAll().stream().filter(it-> it.getCommonCount() > 0).collect(Collectors.toList());
    }


    /**
     *
     * @return
     * Возвращает список неповторяющихся имён.
     * Имена - названия товаров из базы
     * saleproducts
     */
    public Set<String> getProductsNames()
    {
        Set<String> names = new HashSet<>();
        for (SaleProducts prod : getAllActiveProducts())
            names.add(prod.getName());
        return names;
    }

    /**
     *
     * @param name
     * Параметр - наименование товара
     * @return
     * Возвращает список неповторяющихся
     * вкусов, которые характерны для параметра
     * (Наименования товара)
     */
    public Set<String> getProductTastes(String name)
    {
        Set<String> names = new HashSet<>();
        for (SaleProducts prod : getAllActiveProducts())
            if (prod.getName().equals(name))
                names.add(prod.getTaste());
        return names;
    }

    /**
     *
     * @param name
     * Параметр - название товара
     * @param taste
     * Параметр - название вкуса
     * @return
     * Возвращает список всех затяжек, принадлежащих
     * параметру (товару) и определенному параметру (вкусу)
     * товара.
     */
    public Set<Long> getProductSmokes(String name, String taste)
    {
        Set<Long> smokes = new HashSet<>();
        for (SaleProducts prod : getAllActiveProducts())
            if (prod.getName().equals(name) && prod.getTaste().equals(taste))
                smokes.add(prod.getCountSmokes());
        return smokes;
    }

    /**
     *
     * @param name
     * Параметр - название товара
     * @param smokes
     * Параметр - количество затяжек у товара
     * @return
     * Возвращает продукт, который имеет такой же название
     * количество затяжек и вкус
     */
    public SaleProducts getProduct(String name, String taste, Long smokes)
    {
        for (SaleProducts prod : saleProductsInterface.findAll())
            if (prod.getName().equals(name) && prod.getTaste().equals(taste) && prod.getCountSmokes().equals(smokes))
                return prod;
        return null;
    }

    /**
     *
     * @param product
     * Параметр - объект класса SaleProducts
     * @param addCount
     * Функция предназначена для внесения изменений
     * в количество товара в зависимости от того, что
     * делает пользователь. Изменения напрямую вносятся
     * в базу данных saleproducts.
     */
    public void changeProductCount(SaleProducts product, boolean addCount)
    {
        for (SaleProducts activeProd : saleProductsInterface.findAll())
        {
            if (activeProd.getName().equals(product.getName()))
                if (activeProd.getCountSmokes().equals(product.getCountSmokes()))
                    if (activeProd.getTaste().equals(product.getTaste()))
                    {
                        if (addCount)   activeProd.setCommonCount(activeProd.getCommonCount() + 1);
                        else            activeProd.setCommonCount(activeProd.getCommonCount() - 1);
                        saleProductsInterface.save(activeProd);
                        break;
                    }
        }
    }

    /**
     * @param products
     * Параметр - объект класса Package
     * (Может содержать больше одного продукта)
     * @param addCount
     * Параметр - переменная логического типа
     *
     * Функция предназначена для внесения изменений
     * в количество товара в зависимости от того, что
     * делает пользователь. Изменения напрямую вносятся
     * в базу данных saleproducts.
     */
    public void changeProductsCount(Package products, boolean addCount)
    {
        for (SaleProducts prod : products.getUserBasket())
        {
            if (prod != null) changeProductCount(prod, addCount);
        }
    }

    /**
     * Функция позволяет обновить таблицу
     * saleproducts по убыванию общего
     * количества товара.
     */
    public void reUpdateTable()
    {
        List<SaleProducts> list = getAllProductsDesc();
        saleProductsInterface.deleteAll();
        saleProductsInterface.saveAll(list);
    }

    public String getCommonRemains()
    {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        for (SaleProducts prod : getAllProductsDesc())
        {
            buffer.append(++counter)
                  .append(". ")
                  .append(prod.getName())
                  .append(", ")
                  .append(prod.getTaste())
                  .append(", ")
                  .append(prod.getCountSmokes())
                  .append(", ")
                  .append(prod.getCommonCount())
                  .append("\n");
        }
        return buffer.toString();
    }

}
