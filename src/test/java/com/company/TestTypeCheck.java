package com.company;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.MiniJavaParser;
import parser.ParseException;
import syntaxtree.Node;
import visitor.PrettyPrinter;
//import ;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestTypeCheck {

    Node root = null;
//    @BeforeAll
//    public void initialize() {
//
//    }

    @Test
    public void someTest() {

        try {
            FileInputStream f = new FileInputStream("src/test/resources/CaseX.txt");
            root = new MiniJavaParser(f).Goal();
            SymbolTableVisitor sv = new SymbolTableVisitor();
            root.accept(sv, null);
            TypeCheckVisitor tcv = new TypeCheckVisitor();
            root.accept(tcv, null);
            SymbolTable symbolTable = sv.getSymbolTable();
            MiniJavaParser.ReInit(f);
            return;
        } catch (Exception e) {
            fail("Error: should not fail");
        }
    }

    @Test
    public void someTest1() {
        try {
            FileInputStream f = new FileInputStream("src/test/resources/CaseDuplicateField.txt");
            root = new MiniJavaParser(f).Goal();
            SymbolTableVisitor sv = new SymbolTableVisitor();
            assertThrows(TypeCheckException.class, () -> root.accept(sv, null), "Duplicate field name: should throw exception");
        } catch (ParseException e) {

        } catch (TypeCheckException e) {

        } catch (IOException e) {

        }
    }
}
