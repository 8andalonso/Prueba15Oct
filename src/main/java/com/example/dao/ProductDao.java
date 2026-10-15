package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entities.Product;

public interface ProductDao extends JpaRepository<Product, Integer> {

    /**
     * Vamos a necesitar tres metodos personalizados
     * que recuperen la presentacion en una sola consulta, que es mas eficiente, no no?
     * Bueno, no es mas eficiente pero si mas rapido estara disponible la presentacion
     * para cada uno de los productos. Mas eficiente no es porque se consumen mas recursos
     * , puesto que con el patron LAZY (perezoso) se trae la entidad principal e hibernate 
     * no trae las entidades relacionadas hasta su primer uso, y se requiere mas velocidad
     * podemos mejorar esta caracteristica trayendo todo en una sola consulta.
     * 
     * Y los metodos personalizados seran los siguientes:
     * 
     * 1- Metodo que recupera los productos paginados y ordenados tambien, 
     * es decir, de 10 en 10, de 20 en 20, etc. 
     * 
     * 2- Metodo que recupera los productos ordenados, sin paginacion. Ordenados
     * por el nombre, por ejemplo, porque podria ser por cualquier otro campo de la entidad
     * o por varios campos inclusive (stock, por ejemplo)
     * 
     * 3- Dado el id del producto, recupere el producto con su presentacion 
     * correspondiente.
     * 
     * Para implementar los metodos anteriores vamos a hacer uso de HQL/JPQL, que son 
     * lenguajes basados en SQL pero orientados a las entidades y no a las tablas de la
     * base de datos, es decir, que se consultan las entidades y no las tablas de la 
     * base de datos como en SQL
     */

    // Metodo 1. Recupera los producto paginados
    @Query(value = "select p from Product p left join fetch p.presentation", 
        countQuery = "select count(p) from Product p left join p.presentation"
    )
    public Page<Product> findAll(Pageable pageable);

    // Metodo 2. Recupera los productos ordenados, sin paginacion
    @Query(value = "select p from Product p left join fetch p.presentation")
    public List<Product> findAll(Sort sort);

    // Metodo 3. Dado el id de un producto que se recibe como parametro
    // recupera el producto con su presentacion correspondiente
    @Query(value = "select p from Product p left join fetch p.presentation where p.id = :id")
    public Product findById(int id);
    
    
    /**
     * METODO 4. Dado el nombre de una presentacion, que se recibe como parametro,
     * recupera todos los productos que tienen esa presentacion, por ejemplo,
     * todos los productos cuya presentacion sea "unidad".
     * 
     * Igual que en los metodos anteriores, utilizamos HQL/JPQL y "left join fetch"
     * para traer cada producto con su presentacion en una sola consulta, en lugar
     * de que hibernate tenga que ir a buscar la presentacion de cada producto
     * despues (patron LAZY).
     * 
     * En la clausula where se navega por la relacion p.presentation.name, es decir,
     * se consulta la propiedad name de la entidad Presentation asociada al producto,
     * y no una columna de una tabla, porque JPQL trabaja con entidades.
     * 
     * El parametro :presentation se enlaza con el argumento del metodo que tiene
     * el mismo nombre (presentation).
     */
    @Query(value = "select p from Product p left join fetch p.presentation where p.presentation.name = :presentation")
    public List<Product> findByPresentationName(String presentation);
  
}


