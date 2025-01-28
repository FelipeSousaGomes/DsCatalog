package br.devsuperior.dscatalog.utils;

import br.devsuperior.dscatalog.entities.Product;
import br.devsuperior.dscatalog.repositories.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static List<Product> replace(List<ProductProjection> ordered, List<Product> unodereed) {

        Map<Long,Product> map = new HashMap<>();
        for (Product obj: unodereed) {
            map.put(obj.getId(), obj);
        }
        List<Product> result = new ArrayList<>();
        for (ProductProjection obj: ordered) {
            result.add(map.get(obj.getId()));
        }
        return result;
    }
}
