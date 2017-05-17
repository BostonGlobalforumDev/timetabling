/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package log;

/**
 *
 * @author vimi
 *
 * records important informations for analysing in every iteration
 *
 */
public class IterationConfiguration {
    // visualize Neighborhood moves
    public int IterationNum;
    public int MoveType; // Operator Type
    public float OFValue;

    public int HC_Violations;
    public int SC_Violations;


    // Tabu search configuration
    public int tabu_length;
    public int tabu_tenure;
    public int alpha; // in objective function
    public int instance_density;

}
