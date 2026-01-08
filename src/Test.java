public class Test {
    
    public static void main(String[] args) {
        
        Inventory inv = new Inventory();
        Gacha g = new Gacha(inv);

        Item i1 = new Item("burger", "food", 10);
        Item i2 = new Item("fries", "food", 5);
        Item i3 = new Item("soda", "food", 3);
        Item i4 = new Item("teddy bear", "gift", 20);
        Item i5 = new Item("toy car", "gift", 15);

        inv.addNewItem(i1);
        inv.addNewItem(i2);
        inv.addNewItem(i3);
        inv.addNewItem(i4);
        inv.addNewItem(i5);
        g.addNewItem(i1, 10);
        g.addNewItem(i2, 10);
        g.addNewItem(i3, 10);
        g.addNewItem(i4, 10);
        g.addNewItem(i5, 10);

        Pet dino = new Pet("John", "dinosaur", inv, g);

        System.out.println(dino.getName() + " is a " + dino.getType() + " with a score of " + dino.getScore());

        System.out.println(dino.getName() + " has " + dino.getInventory().getItemQuantity("burger") + " burgers.");

        Item newItem = dino.getGacha().rollGacha();

        System.out.println(dino.getName() + " rolled a " + newItem.getItemName() + " from the gacha.");
        
        System.out.println(dino.getName() + " has " + dino.getInventory().getItemQuantity(newItem.getItemName()) + " " + newItem.getItemName() + "s."); 

        dino.giveItem(newItem.getItemName());

        System.out.println(dino.getName() + " now has " + dino.getInventory().getItemQuantity(newItem.getItemName()) + " " + newItem.getItemName() + "s.");

        System.out.println(dino.getName() + " has a happiness of " + dino.getHappiness() + " and a fullness of " + dino.getFullness() + ".");
        
        

    }


}
