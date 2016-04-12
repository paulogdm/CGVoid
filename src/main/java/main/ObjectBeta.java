package main;

import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.jwavefront.JWavefrontObject;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.Cube;
import br.usp.icmc.vicg.gl.model.SimpleModel;
import br.usp.icmc.vicg.gl.model.Sphere;
import br.usp.icmc.vicg.gl.model.WiredCube;
import br.usp.icmc.vicg.gl.util.Shader;
import br.usp.icmc.vicg.gl.util.ShaderFactory;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import main.ship;
import main.planet;

public class ObjectBeta implements GLEventListener {

    private static final int ANIMATION_START_TIME = 3;  
    private float counter;
//padrão: 3  
    
    private enum animationStep {

    START,
    LAND,
    };
    
    animationStep aStep;
    private Shader shader;
    private Matrix4 modelMatrix;//Matrix4 é implementacao da primeira e da segunda prova
    private Matrix4 projectionMatrix;
    private Matrix4 viewMatrix;
    private Light light;
    
    private simpleObject planet;
    
    private ship main_ship;
    private planet moon;
//    private ship view_ship;
    
    private long timeStart;
    private long timeEnd;
    private long timeDiff;
    private int seconds;
    private int minutes;
    
    public ObjectBeta() {
        aStep = animationStep.START;
        shader = ShaderFactory.getInstance(ShaderFactory.ShaderType.COMPLETE_SHADER);
        modelMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        viewMatrix = new Matrix4();
//        light = new Light();
        
        main_ship = new ship();
//        view_ship = new ship();
        
        moon = new planet();
        
        timeStart = System.currentTimeMillis();
        timeEnd = System.currentTimeMillis();
        timeDiff = 0;
        
        counter = 0;
    }
   
    @Override
    //quando ele começar a desenhar ele vai setar o init
    public void init(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();//
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClearDepth(1.0f);
        
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        
        shader.init(gl);
        shader.bind();//indica que shader esta ativo   
        
        modelMatrix.init(gl,shader.getUniformLocation("u_modelMatrix"));
        projectionMatrix.init(gl, shader.getUniformLocation("u_projectionMatrix"));
        viewMatrix.init(gl, shader.getUniformLocation("u_viewMatrix"));
        
        try {
            
            main_ship.getObj().getReady(gl, shader);
            moon.getObj().getReady(gl, shader);
//            view_ship.getObj().getReady(gl, shader);
            
        } catch (IOException ex) {
            Logger.getLogger(ObjectBeta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //init the light
        /*
        light.setPosition(new float[]{10, 10, 50, 1.0f});
        light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
        light.setDiffuseColor(new float[]{0.75f, 0.75f, 0.75f, 1.0f});
        light.setSpecularColor(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
        light.init(gl, shader);
        */
    }
    
    @Override
    public void display(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();//pega gl3 pq pega todas as capacidades de mexer no shader
        
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
       
        // VARIABLE UPDATES ---------------
        
        moon.getObj().updatePosition();    
        
        projectionMatrix.loadIdentity();
        projectionMatrix.ortho(
                -2.0f, 2.0f, 
                -2.0f, 2.0f, 
                -2 * 2.0f, 2 * 2.0f);
        projectionMatrix.bind();
        
        timeEnd = System.currentTimeMillis();
        timeDiff = timeEnd - timeStart;
        seconds = (int)(timeDiff / 1000) % 60;
        minutes = (int)(timeDiff / 60000) % 60;
        
        main_ship.firstLand(ANIMATION_START_TIME, minutes, seconds);
//        view_ship.update();
//        switch (aStep) {
//            case START:
//                if (seconds == Objetoteste.ANIMATION_START_TIME && minutes == 0) {  //Espera para começar apresentação do main_ship
//                    main_ship.setSpeed(simpleObject.KEEP_VALUE, 0.1f, simpleObject.KEEP_VALUE);
//                    main_ship.setPosition(simpleObject.KEEP_VALUE, 0.01f, simpleObject.KEEP_VALUE);
//                    
//                    aStep = animationStep.POKEMON1_PRESENTATION;
//                }
//                break;
//            case POKEMON1_PRESENTATION: //main_ship se apresenta (pula e roda)
//                
//                if (main_ship.getPosition()[1] > 0f) {    //Aplica gravidade
//                    main_ship.addSpeed(0f, -0.003f, 0f);
//                }
//                else {
//                    main_ship.setSpeed(simpleObject.KEEP_VALUE, 0f, simpleObject.KEEP_VALUE);
//                    main_ship.setPosition(simpleObject.KEEP_VALUE, 0f, simpleObject.KEEP_VALUE);
//                }
//                
//                if (main_ship.getPosition()[1] > 1f) {    //Faz rodar o main_ship
//                    main_ship.addRotation(8.2f, 0f, 0f);
//                }
//                else {
//                    main_ship.setRotation(0f, simpleObject.KEEP_VALUE, simpleObject.KEEP_VALUE);                   
//                }
//                
//                if (seconds == Objetoteste.ANIMATION_START_TIME + 4 && minutes == 0) {
//                    aStep = animationStep.POKEMON2_PRESENTATION;
//                }
//                break;
//            case POKEMON2_PRESENTATION: //planet se apresenta (balança pra um lado e pro outro)
//                if(planet.getAuxFactor() == 0f) {  //Balança pra um lado
//                    planet.addRotation(0f, 0f, 5f);
//                }
//                else if(planet.getAuxFactor() == 1f) { //Balança pro outro
//                    planet.addRotation(0f, 0f, -5f);
//                }
//                else if(planet.getAuxFactor() == 2f) { //Balança pro mesmo lado da primeira vez
//                    planet.addRotation(0f, 0f, 5f);
//                    
//                    if(planet.getRotation()[2] > 0f) {
//                        planet.setAuxFactor(3f);
//                        planet.setRotation(simpleObject.KEEP_VALUE, simpleObject.KEEP_VALUE, 0f);
//                    }
//                }
//                
//                if(planet.getRotation()[2] > 45f) {
//                    planet.setRotation(simpleObject.KEEP_VALUE, simpleObject.KEEP_VALUE, 45f);
//                    planet.setAuxFactor(1f);
//                }
//                else if(planet.getRotation()[2] < -45f) {
//                    planet.setRotation(simpleObject.KEEP_VALUE, simpleObject.KEEP_VALUE, -45f);
//                    planet.setAuxFactor(2f);
//                }
//                
//                if (seconds == Objetoteste.ANIMATION_START_TIME + 7 && minutes == 0) {
//                    planet.setAuxFactor(0f);
//                    main_ship.setAuxFactor(1f);
//                    
//                    aStep = animationStep.POKEMON1_ATTACK1;
//                }
//                break;
//            case POKEMON1_ATTACK1:  //main_ship lança raio
//                if (main_ship.getAuxFactor() == 1f) { //Pikachu se prepara pra lançar o raio
//                    main_ship.addRotation(-0.5f, 0f, 0f);
//                    
//                    if (main_ship.getRotation()[0] < -15f) {
//                        main_ship.setAuxFactor(2f);
//                        thunder.setDrawable(true);
//                        thunder.addSpeed(0f, 0f, 0.2f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 2f) {    //Pikachu vai pra frente e raio é lançado
//                    main_ship.addRotation(3f, 0f, 0f);
//                                        
//                    if (main_ship.getRotation()[0] > 20f) {
//                        main_ship.setAuxFactor(3f);
//                        thunder.setSpeed(0f, 0f, 0f);
//                        thunder.setDrawable(false);
//                        planet.setAuxFactor(1f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 3f && main_ship.getRotation()[0] > 0f) {   //Pikachu volta pra posição original
//                    main_ship.addRotation(-2f, 0f, 0f);
//                    
//                    if(planet.getAuxFactor() == 1f) {
//                        planet.addPosition(0.05f, 0f, 0f);
//                        planet.setAuxFactor(2f);
//                    }
//                    else if(planet.getAuxFactor() == 2f) {
//                        planet.addPosition(-0.05f, 0f, 0f);
//                        planet.setAuxFactor(1f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 3f) {
//                    main_ship.setRotation(0f, simpleObject.KEEP_VALUE, simpleObject.KEEP_VALUE);
//                    main_ship.setAuxFactor(4f);
//                    planet.setAuxFactor(0f);
//                    planet.setPosition(0f, 0f, 2.0f);
//                }
//                else if (main_ship.getAuxFactor() == 4f && seconds >= Objetoteste.ANIMATION_START_TIME + 11 && minutes >= 0) {
//                    planet.addSpeed(0f, 0.2f, -0.1f);
//                    planet.addPosition(0f, 0.01f, 0f);
//                    planet.setAuxFactor(1f);
//                    
//                    main_ship.setAuxFactor(0f);
//                    
//                    aStep = animationStep.POKEMON2_ATTACK1;
//                }
//                break;
//            case POKEMON2_ATTACK1:  //planet dá barrigada
//                
//                if (planet.getAuxFactor() == 1f) { //Cria gravidade
//                    if (planet.getSpeed()[1] > 0f) {
//                        planet.addSpeed(0f, -0.01f, 0f);
//                        planet.addRotation(-2f, 0f, 0f);
//                    }
//                    else {
//                        planet.addSpeed(0f, -0.01f, 0f);
//                        planet.addRotation(-2f, 0f, 0f);
//                        planet.setAuxFactor(2f);
//                    }
//                }
//                else if (planet.getAuxFactor() == 2f) {
//                    if(planet.getPosition()[1] > 0.7f) {   //Verifica quando o planet cair sobre o main_ship
//                        planet.addSpeed(0f, -0.01f, 0f);
//                        planet.addRotation(-2f, 0f, 0f);                        
//                    }
//                    else {  //Derruba o main_ship
//                        planet.setSpeed(0f, 0f, 0f);
//                        planet.setPosition(simpleObject.KEEP_VALUE, 0.7f, simpleObject.KEEP_VALUE);
//                        
//                        main_ship.addRotation(-90f, 0f, 0f);
//                        main_ship.addPosition(0f, -0.8f, 0f);
//                         
//                        planet.setAuxFactor(3f);
//                    }
//                }
//                else if (planet.getAuxFactor() == 3f) {
//                    if (seconds == Objetoteste.ANIMATION_START_TIME + 13 && minutes == 0) {   
//                        planet.setAuxFactor(4f);
//                    }
//                }
//                else if (planet.getAuxFactor() == 4f) {
//                    if (planet.getRotation()[0] < 0f) {    //Pansear levanta
//                        planet.addRotation(5f, 0f, 0f);
//                        
//                        planet.addPosition(0f, -0.046f, 0.1f);
//                        //executa 15 vezes
//                    }
//                    else {  //Pansear volta para a posição original
//                        planet.setRotation(0f, simpleObject.KEEP_VALUE, simpleObject.KEEP_VALUE);
//                        planet.setPosition(0f, 0f, simpleObject.KEEP_VALUE);
//                        planet.setSpeed(0f, 0f, 0.1f);
//                        planet.setAuxFactor(5f);
//                    }
//                }
//                else if (planet.getAuxFactor() == 5f) {    //Pansear chega na posição
//                    if (planet.getPosition()[2] > 2.0f) {
//                        planet.setSpeed(0f, 0f, 0f);
//                        planet.setPosition(0f, 0f, 2f);
//                        
//                        planet.setAuxFactor(6f);
//                    }
//                }
//                else if (planet.getAuxFactor() == 6f) {
//                    if (main_ship.getRotation()[0] >= 0f) {
//                        main_ship.setRotation(0f, 0f, 0f);
//                        main_ship.setPosition(simpleObject.KEEP_VALUE, 0f, simpleObject.KEEP_VALUE);
//                        
//                        planet.setAuxFactor(7f);
//                    }
//                    else {  // main_ship levanta
//                        main_ship.addRotation(10f, 0f, 0f);
//                        main_ship.addPosition(0f, 0.088f, 0f);
//                    }
//                }
//                else if (planet.getAuxFactor() == 7f && seconds >= Objetoteste.ANIMATION_START_TIME + 16 && minutes >= 0) {   
//                    planet.setAuxFactor(0f);
//                    main_ship.setSpeed(0f, 0f, 0.05f);
//        
//                    aStep = animationStep.POKEMON1_ATTACK2;
//                }
//                break;
//            case POKEMON1_ATTACK2:  //Pikachu ataca com cauda de ferro
//                if(planet.getAuxFactor() == 0f) {  //PAnsear toma dano
//                    if (main_ship.getRotation()[1] % 360 == 160) {
//                        planet.setAuxFactor(1f);
//                    }
//                }
//                else if(planet.getAuxFactor() == 1f) {
//                    if (planet.getRotation()[0] <= 15f) {
//                        planet.addRotation(5f, 0f, 0f);
//                    }
//                    else {
//                        planet.setAuxFactor(2f);
//                    }
//                }
//                else if(planet.getAuxFactor() == 2f) {
//                    if (planet.getRotation()[0] > 0f) {
//                        planet.addRotation(-5f, 0f, 0f);
//                    }
//                    else {
//                        planet.setAuxFactor(0f);
//                    }
//                }
//                
//                if (main_ship.getAuxFactor() == 0f) { //Pikachu chega no planet
//                    if(main_ship.getPosition()[2] > 0.6f) {
//                        main_ship.setAuxFactor(1f);
//                        main_ship.setSpeed(0f, 0f, 0f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 1f) {    //Pikachu roda devagar
//                    main_ship.addRotation(0f, 10f, 0f);
//                    
//                    if (main_ship.getRotation()[1] >= 540) {
//                        main_ship.setAuxFactor(2f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 2f) {    //Pikachu roda mais rápido
//                    main_ship.addRotation(0f, 20f, 0f);
//                    
//                    if (main_ship.getRotation()[1] >= 1350) {
//                        main_ship.setAuxFactor(3f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 3f) {    //Pikachu desacelera
//                    main_ship.addRotation(0f, 10f, 0f);
//                    
//                    if (main_ship.getRotation()[1] >= 1400) {
//                        main_ship.setAuxFactor(4f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 4f) {    //Desacelera mais
//                    main_ship.addRotation(0f, 5f, 0f);
//                    
//                    if (main_ship.getRotation()[1] >= 1430) {
//                        main_ship.setAuxFactor(5f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 5f) {    //Muito lento
//                    main_ship.addRotation(0f, 2f, 0f);
//                    
//                    if (main_ship.getRotation()[1] >= 1440) {
//                        main_ship.setSpeed(0f, 0f, -0.05f);
//                        
//                        main_ship.setAuxFactor(6f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 6f) {    //Pikachu volta até seu lugar
//                    if (main_ship.getPosition()[2] < -2f) {
//                        main_ship.setSpeed(0f, 0f, 0f);
//                        main_ship.setPosition(0f, 0f, -2f);
//                        main_ship.setAuxFactor(7f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 7f && seconds >= Objetoteste.ANIMATION_START_TIME + 24 && minutes >= 0) {   
//                    planet.setAuxFactor(0f);  
//                    main_ship.setAuxFactor(0f);
//                    
//                    planet.setSpeed(0f, 0f, -0.5f);
//                    
//                    aStep = animationStep.POKEMON2_ATTACK2;
//                }
//                break;
//            case POKEMON2_ATTACK2:  //Pansear ataca loucão
//                if (planet.getAuxFactor() == 0f) { //Pansear alcança o main_ship
//                    if (planet.getPosition()[2] < -0.8f) {
//                        planet.setSpeed(0f, 0f, 0f);
//                        planet.setAuxFactor(1f);
//                        
//                        main_ship.setRotation(5f, 0f, 0f);
//                    }
//                }
//                else if (planet.getAuxFactor() == 1f) {    //Pansear fica batendo doidão
//                    planet.addRotation(30f, 30f, 30f);
//                    
//                    main_ship.setRotation(-main_ship.getRotation()[0], 0f, 0f); //Pikachu levando dano
//                    
//                    if (planet.getRotation()[0] >= 3000) {
//                        planet.setRotation(0f, 180f, 0f);
//                        planet.setAuxFactor(2f);
//                        planet.setSpeed(0f, 0f, 0.1f);
//                    }
//                }
//                else if (planet.getAuxFactor() == 2f) {    //Pansear voltou para a posição original
//                    if (planet.getPosition()[2] >= 2f) {
//                        planet.setSpeed(0f, 0f, 0f);
//                        planet.setPosition(0f, 0f, 2f);
//                        
//                        main_ship.setRotation(0f, 0f, 0f);
//                    
//                        planet.setAuxFactor(3f);
//                    }
//                }
//                else if (planet.getAuxFactor() == 3f && seconds >= Objetoteste.ANIMATION_START_TIME + 32 && minutes >= 0) {   
//                    planet.setAuxFactor(0f);
//                    planet.setSpeed(0f, 0.2f, 0f);
//                    
//                    main_ship.setSpeed(0f, -0.03f, 0f);
//                    
//                    aStep = animationStep.POKEMON1_FAINT;
//                }
//                break;
//            case POKEMON1_FAINT:
//                if (main_ship.getAuxFactor() == 0f) { //Pikachu cai devagar
//                    main_ship.addRotation(-2f, 0f, 0f);
//                    
//                    if(main_ship.getRotation()[0] <= -10f) {
//                        main_ship.setAuxFactor(1f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 1f) {    //Pikachu cai um pouco mais rápido
//                    main_ship.addRotation(-5f, 0f, 0f);
//                    
//                    if(main_ship.getRotation()[0] <= -45f) {
//                        main_ship.setAuxFactor(2f);
//                    }
//                }
//                else if (main_ship.getAuxFactor() == 2f) {    //Pikachu cai um pouco mais rápido
//                    main_ship.addRotation(-10f, 0f, 0f);
//                    
//                    if(main_ship.getRotation()[0] <= -90f) {
//                        main_ship.setRotation(-90f, 0f, 0f);
//                        main_ship.setAuxFactor(3f);
//                        main_ship.setSpeed(0f, 0f, 0f);
//                    }
//                }
//                
//                if (planet.getAuxFactor() == 0f) { //Aplica gravidade
//                    planet.addSpeed(0f, -0.01f, 0f);
//                    
//                    if(planet.getPosition()[1] <= 0f) {
//                        planet.setPosition(simpleObject.KEEP_VALUE, 0f, simpleObject.KEEP_VALUE);
//                        planet.setSpeed(0f, 0.2f, 0f);
//                    }
//                }
//                break;
//            case ENDING:
//                break;      
//            default:
//                break;
//        }
        // --------------------------------
        
//        viewMatrix.loadIdentity();
//       
//        viewMatrix.lookAt(
//                1, 1, 1,
//                0, 0, 0, 
//                0, 1, 0);
//        viewMatrix.bind();
//           
//        light.bind();
    
    main_ship.focus(viewMatrix);

        modelMatrix.loadIdentity();
        modelMatrix.translate(moon.getObj().getPosition()[0], moon.getObj().getPosition()[1], moon.getObj().getPosition()[2]);
        modelMatrix.rotate(moon.getObj().getRotation()[0],1,0,0);
        modelMatrix.rotate(moon.getObj().getRotation()[1],0,1,0);
        modelMatrix.rotate(moon.getObj().getRotation()[2],0,0,1);
        modelMatrix.scale(moon.getObj().getSize()[0], moon.getObj().getSize()[1], moon.getObj().getSize()[2]);
        modelMatrix.bind();
        moon.getObj().draw();
        
        modelMatrix.loadIdentity();
        modelMatrix.translate(main_ship.getObj().getPosition()[0], main_ship.getObj().getPosition()[1], main_ship.getObj().getPosition()[2]);
        modelMatrix.rotate(main_ship.getObj().getRotation()[0],1,0,0);
        modelMatrix.rotate(main_ship.getObj().getRotation()[1],0,1,0);
        modelMatrix.rotate(main_ship.getObj().getRotation()[2],0,0,1);
        modelMatrix.scale(main_ship.getObj().getSize()[0], main_ship.getObj().getSize()[1], main_ship.getObj().getSize()[2]);
        modelMatrix.bind();
        main_ship.getObj().draw();
        
        modelMatrix.loadIdentity();
        modelMatrix.bind();        
        gl.glFlush();
    }
    
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        moon.getObj().dispose();
        main_ship.getObj().dispose();
//      view_ship.getObj().dispose();
    }
    

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        
    }
}