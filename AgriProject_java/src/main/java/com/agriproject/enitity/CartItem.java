package com.agriproject.enitity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CartItem extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore 
    private Cart cart;

    @ManyToOne
    private Product product;

    private int quantity;

    private double price;

}
/* 
 * 
 * 
 I am in home for 40 days tell me level of planning and things i shuld do 

 5 may to 14 june
 i am btech student, i am here in home for 40 days plan me things
 currently things i can do
    java oracle exam prepareation (i dont know exam but konw that in next 1 month i will not give exam means atleast i have these much time)
dsa revision (expected atleaswt 7 hour weekly)
project (expected atleast 8 hour weekly)
collage exam after vaction its tee (there are 4 subject, also rememeber at end time of this may incrase as closder to dates)
backend + frontend enahanmenet (this can major or key taks i can do)
optoinal start code with harray task
optional learn japanese (these companies come)
learn motorcycle (i am not good at t, i have to learn it)
opttional learn cooking (i am not good at it, i have to learn it)

    my habits
    wakeup with hand + shloaks
    1 mintue plank
    dualingo streak
    1 leetcode + 1 gfg + 1 js question (take atleast 1 hour)
    2 page of engliesh
    10+ pushups
    2 power hour (Actually this can increased to 3 or 4)

also i belive in 3 priorites a daily means priorites more than 3 means there is no proirity
power hour means no phone, no distraction, no social media, no youtube, no music, no anything just clean study or coding



 */