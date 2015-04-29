package junit.kenh.xscript.elements;

import kenh.xscript.annotation.Attribute;

@kenh.xscript.annotation.Exclude(Child3.class)
@kenh.xscript.annotation.Include(value={Child1.class, Child2.class}, number={2,0})
public class Parent extends kenh.xscript.impl.BaseElement {

}
